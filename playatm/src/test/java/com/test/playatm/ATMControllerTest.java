package com.test.playatm;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.playatm.model.TransactionRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class ATMControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("Greetings from Spring Boot!")));
	}

	@Test
	public void transaction() throws Exception {

		TransactionRequest request = new TransactionRequest();
		request.setAccount("123asd");
		request.setAmount(100);
		request.setPin("0000");
		request.setType(TransactionRequest.Type.DEPOSIT);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(request);

		mvc.perform(MockMvcRequestBuilders.post("/transaction").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(json)).andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"details\":\"work in progres\",\"type\":\"NOK\"}")));
	
	}
}
