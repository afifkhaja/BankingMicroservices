package com.example.payment_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final String accountServiceBaseUrl;

    public PaymentService(
            PaymentRepository paymentRepository,
            RestTemplate restTemplate,
            @Value("${account-service.url}") String accountServiceBaseUrl
    ) {
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
        this.accountServiceBaseUrl = accountServiceBaseUrl;
    }

    public Payment createPayment(CreatePaymentRequest request) {
        Payment payment = new Payment(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
        );
        paymentRepository.save(payment);

        BigDecimal amount = request.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Amount must be positive");
            return paymentRepository.save(payment);
        }

        // 1) Try to debit the source account
        try {
            BalanceAdjustmentRequest debitRequest = new BalanceAdjustmentRequest(amount.negate());
            String debitUrl = accountServiceBaseUrl + "/accounts/" +
                    request.getFromAccountId() + "/adjust";

            ResponseEntity<String> debitResponse =
                    restTemplate.postForEntity(debitUrl, debitRequest, String.class);

            if (!debitResponse.getStatusCode().is2xxSuccessful()) {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Debit failed with status " + debitResponse.getStatusCode());
                return paymentRepository.save(payment);
            }
        } catch (HttpStatusCodeException ex) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Debit failed: " + ex.getResponseBodyAsString());
            return paymentRepository.save(payment);
        } catch (Exception ex) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Debit failed: " + ex.getMessage());
            return paymentRepository.save(payment);
        }

        // 2) Try to credit the destination account
        try {
            BalanceAdjustmentRequest creditRequest = new BalanceAdjustmentRequest(amount);
            String creditUrl = accountServiceBaseUrl + "/accounts/" +
                    request.getToAccountId() + "/adjust";

            ResponseEntity<String> creditResponse =
                    restTemplate.postForEntity(creditUrl, creditRequest, String.class);

            if (!creditResponse.getStatusCode().is2xxSuccessful()) {
                // Compensation attempt – best effort
                try {
                    BalanceAdjustmentRequest compensate =
                            new BalanceAdjustmentRequest(amount);
                    String compensateUrl = accountServiceBaseUrl + "/accounts/" +
                            request.getFromAccountId() + "/adjust";
                    restTemplate.postForEntity(compensateUrl, compensate, String.class);
                } catch (Exception ignored) {
                    // log in real system
                }

                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Credit failed with status " + creditResponse.getStatusCode());
                return paymentRepository.save(payment);
            }
        } catch (HttpStatusCodeException ex) {
            // Compensation attempt – best effort
            try {
                BalanceAdjustmentRequest compensate =
                        new BalanceAdjustmentRequest(amount);
                String compensateUrl = accountServiceBaseUrl + "/accounts/" +
                        request.getFromAccountId() + "/adjust";
                restTemplate.postForEntity(compensateUrl, compensate, String.class);
            } catch (Exception ignored) {
            }

            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Credit failed: " + ex.getResponseBodyAsString());
            return paymentRepository.save(payment);
        } catch (Exception ex) {
            try {
                BalanceAdjustmentRequest compensate =
                        new BalanceAdjustmentRequest(amount);
                String compensateUrl = accountServiceBaseUrl + "/accounts/" +
                        request.getFromAccountId() + "/adjust";
                restTemplate.postForEntity(compensateUrl, compensate, String.class);
            } catch (Exception ignored) {
            }

            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Credit failed: " + ex.getMessage());
            return paymentRepository.save(payment);
        }

        // 3) Success
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setFailureReason(null);
        return paymentRepository.save(payment);
    }
}