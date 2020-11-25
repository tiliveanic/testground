package com.test.stake.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Stakes {

	public static int MAX_COUNT = 20;
	public static int MAX_BATCH_STAKES = 10;
	private static Executor executor = Executors.newFixedThreadPool(5);

	private ConcurrentLinkedQueue<Stake> inboundStakes = new ConcurrentLinkedQueue<>();

	private SortedSet<Stake> bestStakes = new TreeSet<>(new StakesComparator());

	public void postStake(Stake newStake) {
		inboundStakes.add(newStake);
		CompletableFuture.runAsync(() -> {
			this.prepareStakes();
		}, executor);
	}

	private synchronized void prepareStakes() {

		// start from what we have
		Map<Integer, Stake> candidateStakesMap = new HashMap<>();
		bestStakes.forEach(stake -> candidateStakesMap.put(stake.getCustomerId(), stake));

		int iteration = 0;
		// as long as there are steaks and we have not processes yet to many iterations
		// in this cycle
		boolean changed = false;
		while (inboundStakes.peek() != null && iteration < MAX_BATCH_STAKES) {
			Stake currentStake = inboundStakes.poll();

			// there is a stake from this customer
			if (candidateStakesMap.get(currentStake.getCustomerId()) != null) {
				// is it better - replace the existing stake // otherwise ignore it
				if (candidateStakesMap.get(currentStake.getCustomerId()).getStake() < currentStake.getStake()) {
					candidateStakesMap.put(currentStake.getCustomerId(), currentStake);
					changed = true;
				}
			} else {
				candidateStakesMap.put(currentStake.getCustomerId(), currentStake);
				changed = true;
			}

			iteration++;
		}
		// nothing changed - nothing to do
		if (!changed)
			return;

		// end of the first stage - trimming ( we might have more stakes ) & sorting the
		// candidates
		SortedSet<Stake> candidateBestStakes = new TreeSet<>(new StakesComparator());
		candidateStakesMap.values().forEach(stake -> candidateBestStakes.add(stake));

		SortedSet<Stake> candidateBestStakesTrimmed = new TreeSet<>(new StakesComparator());
		candidateBestStakes.stream().limit(MAX_COUNT).forEach(stake -> candidateBestStakesTrimmed.add(stake));

		// commit the change
		bestStakes = candidateBestStakesTrimmed;
	}

	public List<Stake> getBestStakes() {
		SortedSet<Stake> localBestStake = bestStakes;
		return new ArrayList<Stake>(localBestStake);
	}

}
