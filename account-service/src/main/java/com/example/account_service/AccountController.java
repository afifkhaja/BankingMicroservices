package com.example.account_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        BigDecimal initialBalance = request.getInitialBalance() != null
                ? request.getInitialBalance()
                : BigDecimal.ZERO;

        Account account = new Account(request.getOwnerName(), initialBalance);
        Account saved = accountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Adjust balance. Positive amount = credit, negative = debit.
     * If debit would make balance negative, returns 400.
     */
    @PostMapping("/{id}/adjust")
    public ResponseEntity<?> adjustBalance(
            @PathVariable Long id,
            @RequestBody BalanceAdjustmentRequest request
    ) {
        return accountRepository.findById(id)
                .map(account -> {
                    BigDecimal amount = request.getAmount();
                    if (amount == null) {
                        return ResponseEntity.badRequest().body("Amount is required");
                    }

                    BigDecimal newBalance = account.getBalance().add(amount);
                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        return ResponseEntity.badRequest().body("Insufficient funds");
                    }

                    account.setBalance(newBalance);
                    accountRepository.save(account);
                    return ResponseEntity.ok(account);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}