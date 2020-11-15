package com.test.playatm.model;

public final class Account {

	private String id;
	private Long balance;

	public Account(String id) {
		this.id = id;
		this.balance = Long.valueOf(0);
	}

	public String getId() {
		return id;
	}

	public Long getBalance() {
		return balance;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + "]";
	}

	public static boolean withdraw(Account account, Long amount) {
		if (account.balance >= amount) {
			account.balance = account.balance - amount;
			return true;
		}
		return false;
	}

	public static boolean deposit(Account account, Long amount) {
		account.balance = account.balance + amount;
		return true;
	}

}
