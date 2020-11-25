package com.test.stake.model;

public final class Stake {

	private Integer customerId;

	private int stake;

	public Stake(Integer customerId, int stake) {
		super();
		this.customerId = customerId;
		this.stake = stake;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public int getStake() {
		return stake;
	}

	public Integer getStakeInteger() {
		return stake;
	}

	@Override
	public String toString() {
		return customerId + "=" + stake;
	}
}
