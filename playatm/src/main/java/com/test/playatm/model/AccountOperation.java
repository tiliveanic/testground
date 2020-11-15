package com.test.playatm.model;

@FunctionalInterface
public interface AccountOperation {

	public boolean operation(Account a, Long amount);
}
