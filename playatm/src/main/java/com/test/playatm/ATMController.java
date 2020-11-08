package com.test.playatm;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ATMController {
	
	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}
