package com.example.payment_service;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long fromAccountId;
	private Long toAccountId;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	private String failureReason;
	
	private Instant createdAt;
	
	protected Payment() {} // JPA

	public Payment(Long fromAccountId, Long toAccountId, BigDecimal amount) {
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
		this.status = PaymentStatus.PENDING;
		this.createdAt = Instant.now();
	}
	
	public Long getId() {return id;}
	
	public Long getFromAccountId() {return fromAccountId;}
	
	public Long getToAccountId() {return toAccountId;}
	
	public BigDecimal getAmount() {return amount;}
	
	public PaymentStatus getStatus() {return status;}
	
	public String getFailureReason() {return failureReason;}
	
	public Instant getCreatedAt() {return createdAt;}
	
	public void setStatus(PaymentStatus status) {this.status = status;}
	
	public void setFailureReason(String failureReason) {this.failureReason = failureReason;}
	
}
