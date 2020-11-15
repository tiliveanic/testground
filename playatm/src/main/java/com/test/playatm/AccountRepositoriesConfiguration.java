package com.test.playatm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountRepositoriesConfiguration {

	@Bean
	public AccountRepository northRepository() {
		return new AccountRepository();
	}

	@Bean
	public AccountRepository southRepository() {
		return new AccountRepository();
	}

}
