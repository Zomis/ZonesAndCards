package test.net.zomis.cards;

import net.zomis.cards.model.CardGame;


public class SimpleGameTest extends CardsTest<CardGame> {

	@Override
	protected CardGame newTestObject() {
		return new CardGame();
	}
	
}
