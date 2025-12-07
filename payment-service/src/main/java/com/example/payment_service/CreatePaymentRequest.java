package com.example.payment_service;

import java.math.BigDecimal;

public class CreatePaymentRequest {

	private Long fromAccountId;
	private Long toAccountId;
	private BigDecimal amount;
	
	public CreatePaymentRequest() {}
	
	public Long getFromAccountId() {return fromAccountId;}
	
	public Long getToAccountId() {return toAccountId;}
	
	public BigDecimal getAmount() {return amount;}
	
	public void setFromAccountId(Long fromAccountId) {this.fromAccountId = fromAccountId;}
	
	public void setToAccountId(Long toAccountId) {this.toAccountId = toAccountId;}
	
	public void setAmount(BigDecimal amount) {this.amount = amount;}
	
}