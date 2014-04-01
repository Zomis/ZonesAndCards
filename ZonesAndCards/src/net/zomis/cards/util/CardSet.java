package net.zomis.cards.util;

import net.zomis.cards.model.CardGame;

public interface CardSet<CG extends CardGame> {
	void addCards(CG game);
}
