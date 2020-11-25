package com.test.stakes.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.test.stake.model.Stakes;
import com.test.stakes.api.StakesService;

public class TestStakesServiceBean {
	
	public StakesService stakesService = new StakesServiceBean();
	
	@BeforeEach
	public void setup()
	{
		Stakes.MAX_COUNT = 3;
	}
	
	
	@Test
	public void testBasic() throws InterruptedException
	{
		stakesService.postStake("100", 100, 50);
		stakesService.postStake("100", 100, 49);
		stakesService.postStake("100", 100, 49);
		stakesService.postStake("100", 100, 49);
		stakesService.postStake("100", 100, 49);
		stakesService.postStake("100", 100, 51);
		
		stakesService.postStake("101", 100, 40);
		stakesService.postStake("101", 100, 41);
		stakesService.postStake("101", 100, 42);
		stakesService.postStake("101", 100, 44);

		
		stakesService.postStake("102", 100, 30);
		stakesService.postStake("103", 100, 20);
		stakesService.postStake("103", 100, 20);
		stakesService.postStake("103", 100, 20);
		stakesService.postStake("103", 100, 20);
		stakesService.postStake("103", 100, 19);
		Thread.sleep(1000);
		assertEquals("[103=19, 102=30, 101=40]", stakesService.getHighStakes(100).toString());
		System.out.println(stakesService.getHighStakes(100));	
	}

}
