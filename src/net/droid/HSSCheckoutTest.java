package net.droid;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class HSSCheckoutTest {

	@Test
	public void testPrice() {
		HSSCheckout tester = new HSSCheckout();		
		assertEquals("ABBBC must be 9.04", new BigDecimal("9.04"), tester.price("ABBBC"));
		
	}
/*
	@Test
	public void testMain() {
		fail("Not yet implemented");
	}
*/
}
