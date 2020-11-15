package com.test.playatm.model;

public final class Account {

	private String id;
	private Long balanace;

	public Account(String id) {
		this.id = id;
		this.balanace = Long.valueOf(0);
	}

	public String getId() {
		return id;
	}

	public Long getBalanace() {
		return balanace;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", balanace=" + balanace + "]";
	}

	public static boolean withdraw(Account account, Long amount) {
		if (account.balanace > amount) {
			account.balanace = account.balanace - amount;
			return true;
		}
		return false;
	}

	public static boolean deposit(Account account, Long amount) {
		account.balanace = account.balanace + amount;
		return true;
	}

}
