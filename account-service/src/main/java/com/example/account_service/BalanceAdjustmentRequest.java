package com.example.account_service;

import java.math.BigDecimal;

public class BalanceAdjustmentRequest {
	
	/**
	 * Positive = credit, negative = debit.
	 */
	private BigDecimal amount;
	
	public BalanceAdjustmentRequest() {}
	
	public BigDecimal getAmount() {return amount;}

	public void setAmount(BigDecimal amount) {this.amount=amount;}
	
}
