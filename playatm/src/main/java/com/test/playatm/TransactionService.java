package com.test.playatm;

import java.util.AbstractMap;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.test.playatm.model.TransactionRequest;

@Service
public class TransactionService {

	private static final Logger LOGGER = LogManager.getLogger(TransactionService.class);

	@Autowired
	private TransactionBus transactionBus;

	@Autowired
	@Qualifier("northRepository")
	private AccountRepository northRepository;

	@Autowired
	@Qualifier("southRepository")
	private AccountRepository southRepository;

	@Scheduled(fixedRate = 100)
	public void processTransactions() {
		AbstractMap.SimpleEntry<Integer, TransactionRequest> transactionRequest = transactionBus.pollTransactionPair();
		while (transactionRequest != null) {
			LOGGER.info("Processing : " + transactionRequest);

			boolean discriminator = transactionRequest.getValue().getAccount().hashCode() % 2 == 0;
			LOGGER.info("Discriminator " + discriminator);

			AccountRepository acountRepository = discriminator ? northRepository : southRepository;
			LOGGER.info("Repository chosen " + acountRepository);

			final AbstractMap.SimpleEntry<Integer, TransactionRequest> current = transactionRequest;

			CompletableFuture.supplyAsync(() -> {
				return acountRepository.processTransaction(current.getValue());
			}, acountRepository.getExecutionPoll()).thenApplyAsync(t -> {
				return t.appendDetails("[Ticket " + current.getKey());
			}).thenAcceptAsync(t -> {
				transactionBus.submitResponse(current.getKey(), t);
			});

			// non-async, single threaded way
//			TransactionResponse response = acountRepository.processTransaction(transactionRequest.getValue());
//			response.appendDetails("[Ticket " + transactionRequest.getKey());
//			transactionBus.submitResponse(transactionRequest.getKey(), response);

			transactionRequest = transactionBus.pollTransactionPair();
		}

	}
}
