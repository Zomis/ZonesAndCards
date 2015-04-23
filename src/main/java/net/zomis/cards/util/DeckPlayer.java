package net.zomis.cards.util;

import net.zomis.cards.model.CardModel;

public interface DeckPlayer<M extends CardModel> {
	int getCardCount();
	void addCard(M card);
}
