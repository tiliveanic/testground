package com.test.stake.model;

public final class Stake {

	private int customerId;
	private int stake;

	public Stake(int customerId, int stake) {
		super();
		this.customerId = customerId;
		this.stake = stake;
	}

	@Override
	public String toString() {
		return customerId + "=" + stake;
	}
}
