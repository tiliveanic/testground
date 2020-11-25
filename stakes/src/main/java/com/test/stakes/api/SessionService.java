package com.test.stakes.api;

import java.util.Optional;

public interface SessionService {

	public String generateSessionKey(int customerId);
	
	public Optional<Integer> validateAndProvideCustomer(String sessionKey);
	
}
