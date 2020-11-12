package com.test.playatm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.playatm.model.TransactionRequest;
import com.test.playatm.model.TransactionResponse;

@RestController
public class ATMController {
	
	private static final Logger LOGGER = LogManager.getLogger(ATMController.class);
	
	@Autowired
	private TransactionBus transactionBus;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";

	}

	@PostMapping(value = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TransactionResponse> transaction(@RequestBody TransactionRequest request) {
		LOGGER.info("Received "+ request);
		
		Integer ticket = transactionBus.submitTransaction(request);
		
		TransactionResponse response = transactionBus.getTransactionResponse(ticket);
		return ResponseEntity.ok(response);
	}
}
