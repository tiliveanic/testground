package com.test.stakes.impl;

import java.util.Optional;

import com.test.stakes.api.SessionService;

/**
 * Trivial implementation for the session key.<br>
 * The sessionKey is the customerId.
 * 
 * @author Nicu
 */
public class SessionServiceBean implements SessionService {

	public String generateSessionKey(int customerId) {
		return "" + customerId;
	}

	public Optional<Integer> validateAndProvideCustomer(String sessionKey) {
		return Optional.of(Integer.valueOf(sessionKey).intValue());
	}

}
