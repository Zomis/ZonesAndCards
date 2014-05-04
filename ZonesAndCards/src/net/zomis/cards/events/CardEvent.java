package net.zomis.cards.events;

import net.zomis.cards.model.Card;

public class CardEvent extends CardGameEvent {

	private final Card<?> card;

	public CardEvent(Card<?> card) {
		super(card.getGame());
		this.card = card;
	}
	
	public Card<?> getCard() {
		return card;
	}
}
