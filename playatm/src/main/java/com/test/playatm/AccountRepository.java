package com.test.playatm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.test.playatm.model.Account;
import com.test.playatm.model.AccountOperation;
import com.test.playatm.model.TransactionRequest;
import com.test.playatm.model.TransactionResponse;
import com.test.playatm.model.TransactionResponse.Type;

public class AccountRepository {

	private static final Logger LOGGER = LogManager.getLogger(AccountRepository.class);

	private Map<String, Account> accounts = new HashMap<>();
	private Executor accountExecutor = Executors.newSingleThreadExecutor();

	public TransactionResponse processTransaction(TransactionRequest transaction) {

		LOGGER.info("Executing " + transaction);

		Account account = accounts.get(transaction.getAccount());
		if (account == null) {
			account = new Account(transaction.getAccount());
			accounts.put(account.getId(), account);
		}

		AccountOperation operation = transaction.getType().getOperation();

		TransactionResponse response;
		if (operation.operation(account, transaction.getAmount())) {
			response = new TransactionResponse();
			response.setType(Type.OK);
			response.setDetails("Account status :" + account);

		} else {
			response = new TransactionResponse();
			response.setType(Type.NOK);
			response.setDetails("Operation failed for " + account.getId());
		}

		return response;
	}

	public Executor getExecutionPoll() {
		return accountExecutor;
	}

}
