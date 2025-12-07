package com.example.account_service;

import java.math.BigDecimal;

public class CreateAccountRequest {

	private String ownerName;
	private BigDecimal initialBalance;
	
	public CreateAccountRequest() {}
	
	public String getOwnerName() {return ownerName;}
	
	public BigDecimal getInitialBalance() {return initialBalance;}
	
	public void setOwnerName(String ownerName) {this.ownerName = ownerName;}
	
	public void setInitialBalance(BigDecimal balance) {this.initialBalance = balance;}
	
}