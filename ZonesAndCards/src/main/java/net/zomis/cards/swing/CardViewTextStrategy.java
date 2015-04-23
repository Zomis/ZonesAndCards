package net.zomis.cards.swing;

import net.zomis.cards.model.Card;

public interface CardViewTextStrategy {

	String textFor(Card<?> card);
	
}
