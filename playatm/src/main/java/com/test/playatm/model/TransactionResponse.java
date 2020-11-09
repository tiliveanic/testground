package com.test.playatm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionResponse {

	@JsonProperty
	public String details;
	@JsonProperty
	public Type type;

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public enum Type {
		OK, NOK;
	}

	@Override
	public String toString() {
		return "TransactionResponse [details=" + details + ", type=" + type + "]";
	}
	
	
}
