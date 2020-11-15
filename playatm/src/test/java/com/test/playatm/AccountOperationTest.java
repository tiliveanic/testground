package com.test.playatm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.test.playatm.model.Account;
import com.test.playatm.model.AccountOperation;
import com.test.playatm.model.TransactionRequest;

public class AccountOperationTest {

	@Test
	public void testOperation() {
		Account a = new Account("test");

		AccountOperation deposit = TransactionRequest.Type.DEPOSIT.getOperation();
		AccountOperation withdraw = TransactionRequest.Type.WITDHRAW.getOperation();

		assertTrue(deposit.operation(a, Long.valueOf(100)));
		assertEquals(Long.valueOf(100), a.getBalanace());

		assertTrue(withdraw.operation(a, Long.valueOf(1)));
		assertEquals(Long.valueOf(99), a.getBalanace());

		assertFalse(withdraw.operation(a, Long.valueOf(100)));
		assertEquals(Long.valueOf(99), a.getBalanace());
	}
}
