package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.factory.Battlecry;

public class ManaZeroCards implements CardSet<HStoneGame> {

	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 0,      NONE, 1, 1, "Chicken").card());
		game.addCard(minion( 0,      NONE, 1, 1, "Violet Apprentice").card());
		game.addCard(minion( 0,    COMMON, 0, 1, "Frog").taunt().card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Murloc Scout").card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Sheep").card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Wisp").card());
		game.addCard(spell( 0,      NONE, "The Coin").effect(e.tempMana(1)).card());
	}

}
