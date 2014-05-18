package net.zomis.cards.hstone.sets;

import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.factory.Battlecry;
import net.zomis.cards.util.CardSet;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;

public class ManaZeroCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 0,      NONE, 1, 1, "Chicken").card());
		game.addCard(minion( 0,      NONE, 1, 1, "Violet Apprentice").card());
		game.addCard(minion( 0,    COMMON, 0, 1, "Frog").taunt().card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Murloc Scout").card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Sheep").card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Wisp").card());
		game.addCard(spell( 0,      NONE, "The Coin").effect(Battlecry.tempMana(1)).card());
	}

}
