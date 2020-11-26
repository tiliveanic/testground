package com.test.stakes.impl;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.test.stake.model.Stakes;
import com.test.stakes.api.StakesService;

public class TestStakeServiceLoad {

	public StakesService stakesService = new StakesServiceBean();

	public Executor readers = Executors.newFixedThreadPool(10);
	public Executor writers = Executors.newFixedThreadPool(30);

	@BeforeEach
	public void setup()
	{
		Stakes.MAX_COUNT = 5;
	}


	private int getRandomBet() {
		return 100 + (int) (10000 * Math.random());
	}

	private int getRandomCustomer() {
		return 200 + (int) (40 * Math.random());
	}

	private int getRandomValue() {
		return (int) (4000 * Math.random());
	}

	@Test
	public void testConncurency() throws InterruptedException {
		final int sleepReaders = 200;

		final int sleepWriters = 50;

		Thread readerSpawner = new Thread(() -> {

			while (true) {
				for (int i = 0; i < 30; i++) {
					readers.execute(() -> {

						randomSleep(sleepReaders);
						System.out.println(stakesService.getHighStakes(getRandomBet()));

					});

				}
				sleep(sleepReaders);
			}

		});
		readerSpawner.start();

		Thread writerSpawner = new Thread(() -> {

			while (true) {
				for (int i = 0; i < 100; i++) {
					writers.execute(() -> {

						randomSleep(sleepWriters);
						stakesService.postStake("" + getRandomCustomer(), getRandomBet(), getRandomValue());
						;

					});

				}
				sleep(sleepWriters);
			}

		});
		writerSpawner.start();

		readerSpawner.join();
		writerSpawner.join();

	}

	private void randomSleep(final int time) {
		try {
			Thread.sleep((int) (Math.random() * time));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void sleep(final int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
