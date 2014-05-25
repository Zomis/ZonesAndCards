package net.zomis.cards;

public class CardsUntypedTest extends CardsTest<Integer> {
	@Override
	protected void onBefore() {
		game = 42;
	}
}
