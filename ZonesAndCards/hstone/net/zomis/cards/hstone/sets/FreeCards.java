package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.util.CardSet;

public class FreeCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion(0, COMMON, 1, 1, "Wisp").card());
	}

}
