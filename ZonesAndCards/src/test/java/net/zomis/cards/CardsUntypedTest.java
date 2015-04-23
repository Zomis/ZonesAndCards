package net.zomis.cards;

import org.junit.Test;
import static org.junit.Assert.*;

public class CardsUntypedTest extends CardsTest<Integer> {
	@Override
	protected void onBefore() {
		game = 42;
	}
	
	@Test
	public void noTestForSillyNetbeans() {
		assertTrue(true);
	}
}
