package net.zomis.cards.events;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;

public class CardEvent extends CardGameEvent {

	private final Card<?> card;

	public CardEvent(Card<?> card) {
		this(card, card.getGame());
	}
	
	public CardEvent(Card<?> card, CardGame<?, ?> cardGame) {
		super(cardGame);
		this.card = card;
	}
	
	public Card<?> getCard() {
		return card;
	}
}
