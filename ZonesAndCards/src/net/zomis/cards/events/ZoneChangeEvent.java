package net.zomis.cards.events;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.events.IEvent;

public class ZoneChangeEvent implements IEvent {
	
	private final CardZone fromCardZone;
	private CardZone toCardZone;
	private final Card card;

	public ZoneChangeEvent(CardZone fromCardZone, CardZone toCardZone, Card card) {
		this.fromCardZone = fromCardZone;
		this.toCardZone = toCardZone;
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
	public CardZone getFromCardZone() {
		return fromCardZone;
	}
	public CardZone getToCardZone() {
		return toCardZone;
	}
	public void setToCardZone(CardZone toCardZone) {
		this.toCardZone = toCardZone;
	}
	@Override
	public String toString() {
		return String.format("ZoneChange{%s: %s-->%s}", this.card, this.getFromCardZone(), this.getToCardZone());
	}
}
