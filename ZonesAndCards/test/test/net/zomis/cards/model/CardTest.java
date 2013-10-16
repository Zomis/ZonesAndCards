package test.net.zomis.cards.model;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.actions.InvalidStackAction;

import org.junit.Test;

import test.net.zomis.cards.CardsTest;

public class CardTest extends CardsTest<CardGame> {

	@Test(expected = IllegalStateException.class)
	public void notStarted() {
		getGame().addAndProcessStackAction(new InvalidStackAction());
	}

	@Override
	protected CardGame newTestObject() {
		return new CardGame();
	}
	
}
