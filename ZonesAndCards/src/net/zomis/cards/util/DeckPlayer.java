package net.zomis.cards.util;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;

public interface DeckPlayer<ECardModel extends CardModel> {
	int getCardCount();
	CardGame<?, ?> getGame();
	CardZone<?> getDeck();
	void addCard(ECardModel card);
	void clearCards();
}
