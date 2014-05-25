package net.zomis.cards.wart.factory;

import net.zomis.cards.wart.HStoneClass;

public class HStoneChars {

	public static HStoneChar jaina(String name) {
		return new HStoneChar(name, HStoneClass.MAGE, HStoneDecks.testDeck());
	}

}
