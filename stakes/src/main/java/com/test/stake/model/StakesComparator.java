package com.test.stake.model;

import java.util.Comparator;

public class StakesComparator implements Comparator<Stake> {

	@Override
	public int compare(Stake o1, Stake o2) {
		return o1.getStakeInteger().compareTo(o2.getStakeInteger());
	}

}
