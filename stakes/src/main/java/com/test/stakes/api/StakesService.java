package com.test.stakes.api;

import java.util.List;

import com.test.stake.model.Stake;

public interface StakesService {

	public void postStake(String sessionKey, int betId, int stake);

	public List<Stake> getHighStakes(int betId);
}
