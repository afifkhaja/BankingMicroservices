package com.example.payment_service;

import java.math.BigDecimal;

public class BalanceAdjustmentRequest {

	private BigDecimal amount;
	
	public BalanceAdjustmentRequest() {}
	
	public BalanceAdjustmentRequest(BigDecimal amount) {this.amount = amount;}
	
	public BigDecimal getAmount() {return amount;}
	
	public void setAmount(BigDecimal amount) {this.amount = amount;}
	
}