package com.test.stakes.api;

public interface SessionService {

	public String generateSessionKey(int customerId);
	
	public String validateAndProvideCustomer(String sessionKey);
	
}
