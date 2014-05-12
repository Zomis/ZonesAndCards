package net.zomis.cards.crgame;

import net.zomis.cards.util.DeckList;

public class CRPlayerFactory {

	public CRPlayer newPlayerPCG(CRCardGame game) {
		DeckList cards = new DeckList();
		cards.add(10, CRCards.ASK_A_QUESTION.cardName());
		CRPlayer player = new CRPlayer("Programming Puzzles & Code Golf", cards);
		return player;
	}

	public CRPlayer newPlayerCodeReview(CRCardGame game) {
		DeckList cards = new DeckList();
		cards.add(10, "Exploding Bear Trap");
		CRPlayer player = new CRPlayer("Code Review", cards);
		return player;
	}

}
