package com.test.stake.app;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.test.stakes.api.StakesService;
import com.test.stakes.impl.StakesServiceBean;

/**
 * 
 * Simulation of the run - copied from TestStakesServicesLoad, just to have a
 * main class.
 *
 */
public class RunSimulator {

	public static void main(String[] args) {

		try {
			new RunSimulator().testConncurency();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public StakesService stakesService = new StakesServiceBean();

	public Executor readers = Executors.newFixedThreadPool(10);
	public Executor writers = Executors.newFixedThreadPool(30);

	private int getRandomBet() {
		return 100 + (int) (10000 * Math.random());
	}

	private int getRandomCustomer() {
		return 200 + (int) (40 * Math.random());
	}

	private int getRandomValue() {
		return (int) (4000 * Math.random());
	}

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
