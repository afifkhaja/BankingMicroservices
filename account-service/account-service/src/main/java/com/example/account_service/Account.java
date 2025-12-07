package com.example.account_service;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String ownerName;
	
	@Column(nullable = false)
	private BigDecimal balance;
	
	protected Account() {} // JPA only
	
	public Account(String ownerName, BigDecimal balance) {
		this.ownerName = ownerName;
		this.balance = balance;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}