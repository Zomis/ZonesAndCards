package net.zomis.cards.crgame;

import java.util.Set;

import net.zomis.cards.util.DeckList;

public class CRPlayerFactory {

	public CRPlayer newPlayerPCG(CRCardGame game) {
		DeckList cards = new DeckList();
		addCardsNotIn(cards, game, CRCards.crCards);
		CRPlayer player = new CRPlayer("Programming Puzzles & Code Golf", cards);
		return player;
	}

	private void addCardsNotIn(DeckList cards, CRCardGame game, Set<CRCardModel> crCards) {
		for (CRCardModel model : game.getCards().values()) {
			if (model.isZombie())
				continue;
			if (crCards.contains(model))
				continue;
			cards.add(1, model);
		}
	}

	public CRPlayer newPlayerCodeReview(CRCardGame game) {
		DeckList cards = new DeckList();
		addCardsNotIn(cards, game, CRCards.pcgCards);
		CRPlayer player = new CRPlayer("Code Review", cards);
		return player;
	}

}
