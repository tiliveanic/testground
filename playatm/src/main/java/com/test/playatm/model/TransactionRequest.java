package com.test.playatm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequest {

	@JsonProperty
	private String account;
	@JsonProperty
	private String pin;
	@JsonProperty
	private long amount;
	@JsonProperty
	private Type type;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public enum Type {
		DEPOSIT, WITDHRAW;
	}

	@Override
	public String toString() {
		return "TransactionRequest [account=" + account + ", pin=" + pin + ", amount=" + amount + ", type=" + type
				+ "]";
	}
	
	

}
