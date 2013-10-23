package net.zomis.cards.events;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.events.IEvent;

public class CardCreatedEvent implements IEvent {

	private final Card	card;
	private final CardZone	zone;
	private final CardGame	game;

	public CardCreatedEvent(final Card card) {
		this.card = card;
		this.zone = card.getCurrentZone();
		this.game = card.getGame();
	}
	
	public Card getCard() {
		return card;
	}
	public CardGame getGame() {
		return game;
	}
	public CardZone getZone() {
		return zone;
	}
}
