package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HStoneClass;

public class HStoneChars {

	public static HStoneChar jaina(String name) {
		return new HStoneChar(name, HStoneClass.MAGE, HStoneDecks.mageDefault());
	}

}
