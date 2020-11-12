package com.test.playatm;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.test.playatm.model.TransactionRequest;
import com.test.playatm.model.TransactionResponse;

@Service
public class TransactionBus {
	private static final Logger LOGGER = LogManager.getLogger(TransactionBus.class);

	private AtomicInteger tickets = new AtomicInteger(0);
	private ConcurrentLinkedQueue<AbstractMap.SimpleEntry<Integer, TransactionRequest>> incomingTransactions = new ConcurrentLinkedQueue<>();
	private Map<Integer, LinkedBlockingQueue<TransactionResponse>> responses = new ConcurrentHashMap<>();

	public Integer submitTransaction(TransactionRequest request) {
		Integer ticket = tickets.getAndIncrement();
		incomingTransactions.offer(new AbstractMap.SimpleEntry<Integer, TransactionRequest>(ticket, request));
		responses.put(ticket, new LinkedBlockingQueue<>());

		return ticket;
	}

	public void submitResponse(Integer ticket, TransactionResponse response) {
		responses.get(ticket).offer(response);
	}
	
	public AbstractMap.SimpleEntry<Integer, TransactionRequest> pollTransactionPair()
	{
		return incomingTransactions.poll();
	}

	public TransactionResponse getTransactionResponse(Integer ticket) {
		TransactionResponse response;
		try {
			response = responses.get(ticket).poll(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOGGER.error("Exception retrieving response.");
			response = new TransactionResponse();
			response.setType(TransactionResponse.Type.NOK);
			response.setDetails("Interrupted while getting transaction response.");
		}
		// no response in a reasonable time
		if (response == null) {
			response = new TransactionResponse();
			response.setType(TransactionResponse.Type.NOK);
			response.setDetails("Timeout waiting for the transaction response.");
		}
		return response;
	}

}
