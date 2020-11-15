package com.test.playatm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.test.playatm.model.TransactionRequest;
import com.test.playatm.model.TransactionRequest.Type;
import com.test.playatm.model.TransactionResponse;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
class LoadTest {

	private static final Logger LOGGER = LogManager.getLogger(LoadTest.class);
	@Autowired
	TransactionBus transactionBus;

	@Autowired
	TransactionService transactionService;

	private Map<Integer, String> expected = new ConcurrentHashMap<>();

	private void generateTransactions(int id) throws InterruptedException {
		List<AbstractMap.SimpleEntry<TransactionRequest, String>> requests = new ArrayList<>();
		// for an acount - id
		String account = "acount" + id;

		// deposit 100
		requests.add(new AbstractMap.SimpleEntry<TransactionRequest, String>(
				new TransactionRequest(account, 100, Type.DEPOSIT), "balance=100"));
		// withdraw 101
		requests.add(new AbstractMap.SimpleEntry<TransactionRequest, String>(
				new TransactionRequest(account, 101, Type.WITDHRAW), "Operation failed"));
		// withdraw 99
		requests.add(new AbstractMap.SimpleEntry<TransactionRequest, String>(
				new TransactionRequest(account, 99, Type.WITDHRAW), "balance=1"));
		// deposit 1
		requests.add(new AbstractMap.SimpleEntry<TransactionRequest, String>(
				new TransactionRequest(account, 1, Type.DEPOSIT), "balance=2"));
		// withdraw 2
		requests.add(new AbstractMap.SimpleEntry<TransactionRequest, String>(
				new TransactionRequest(account, 2, Type.WITDHRAW), "balance=0"));

		Thread.sleep((long) (Math.random() * 10));

		requests.forEach(t -> {
			Integer ticket = transactionBus.submitTransaction(t.getKey());
			expected.put(ticket, t.getValue());
		});
	}

	@Test
	void doManyTransactions() {

		// prepare
		List<CompletableFuture<Void>> generators = new ArrayList<>();
		// prepare ( 10_000 account operations)
		for (int i = 0; i < 10_000; i++) {
			final int id = i;

			CompletableFuture<Void> generate = CompletableFuture.runAsync(() -> {
				try {
					generateTransactions(id);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}, Executors.newFixedThreadPool(10));
			generators.add(generate);
		}

		generators.forEach(g -> {
			g.join();
		});

		// act
		transactionService.processTransactions();

		// assert
		AtomicInteger counter = new AtomicInteger();
		expected.keySet().forEach(t -> {
			TransactionResponse response = transactionBus.getTransactionResponse(t);
			LOGGER.info(response.getDetails());
			assertTrue(response.getDetails().contains(expected.get(t)));
			counter.addAndGet(1);
		});

		assertEquals(50_000, counter.get());
	}

}
