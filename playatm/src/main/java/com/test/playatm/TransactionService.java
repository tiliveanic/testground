package com.test.playatm;

import java.util.AbstractMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.test.playatm.model.TransactionRequest;
import com.test.playatm.model.TransactionResponse;

@Service
public class TransactionService {

	private static final Logger LOGGER = LogManager.getLogger(TransactionService.class);

	@Autowired
	TransactionBus transactionBus;

	@Scheduled(fixedRate = 100)
	public void processTransactions() {
		AbstractMap.SimpleEntry<Integer, TransactionRequest> transactionRequest = transactionBus.pollTransactionPair();
		while (transactionRequest != null) {
			LOGGER.info("Processing : " + transactionRequest);

			TransactionResponse response = new TransactionResponse();
			response.setType(TransactionResponse.Type.OK);
			response.setDetails("Transaction was opperated. Ticket = " + transactionRequest.getKey());

			transactionBus.submitResponse(transactionRequest.getKey(), response);
			transactionRequest = transactionBus.pollTransactionPair();
		}

	}
}
