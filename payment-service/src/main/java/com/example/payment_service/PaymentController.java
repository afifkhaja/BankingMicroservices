package com.example.payment_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentRepository paymentRepository;
	private final PaymentService paymentService;
	
	public PaymentController(PaymentRepository paymentRepository, PaymentService paymentService) {
		this.paymentRepository = paymentRepository;
		this.paymentService = paymentService;
	}
	
	@PostMapping
	public ResponseEntity<Payment> createPayment(@RequestBody CreatePaymentRequest request){
		Payment payment = paymentService.createPayment(request);
		HttpStatus status = payment.getStatus() == PaymentStatus.FAILED
				? HttpStatus.BAD_REQUEST
				: HttpStatus.CREATED;
		return ResponseEntity.status(status).body(payment);
	}

	@GetMapping
	public List<Payment> getAllPayments(){
		return paymentRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Payment> getPayment(@PathVariable Long id){
		return paymentRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
}
