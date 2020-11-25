package com.test.stakes.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.test.stake.model.Stake;
import com.test.stake.model.Stakes;
import com.test.stakes.api.SessionService;
import com.test.stakes.api.StakesService;

public class StakesServiceBean implements StakesService {

	private SessionService sessionService = new SessionServiceBean();
	private Map<Integer, Stakes> stakes = new ConcurrentHashMap<Integer, Stakes>();

	public void postStake(String sessionKey, int betId, int stakeValue) {
		Optional<Integer> customer = sessionService.validateAndProvideCustomer(sessionKey);

		if (customer.isPresent()) {
			Stake stake = new Stake(customer.get(), stakeValue);

			stakes.putIfAbsent(Integer.valueOf(betId), new Stakes());
			stakes.get(Integer.valueOf(betId)).postStake(stake);
		}
	}

	public List<Stake> getHighStakes(int betId) {
		return stakes.get(Integer.valueOf(betId)).getBestStakes();
	}

}
