package com.test.playatm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequest {

	@JsonProperty
	private String account;
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
		DEPOSIT(Account::deposit), WITDHRAW(Account::withdraw);

		private AccountOperation operation;

		private Type(AccountOperation operation) {
			this.operation = operation;
		}

		public AccountOperation getOperation() {
			return operation;
		}

	}

	@Override
	public String toString() {
		return "TransactionRequest [account=" + account + ", amount=" + amount + ", type=" + type + "]";
	}

}
