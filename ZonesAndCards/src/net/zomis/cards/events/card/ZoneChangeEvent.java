package net.zomis.cards.events.card;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.events.IEvent;

public class ZoneChangeEvent extends CardEvent implements IEvent {
	
	private final CardZone fromCardZone;
	private CardZone toCardZone;

	public ZoneChangeEvent(CardZone fromCardZone, CardZone toCardZone, Card card) {
		super(card);
		this.fromCardZone = fromCardZone;
		this.toCardZone = toCardZone;
	}
	
	public final CardZone getFromCardZone() {
		return fromCardZone;
	}
	public final CardZone getToCardZone() {
		return toCardZone;
	}
	public final void setToCardZone(CardZone toCardZone) {
		this.toCardZone = toCardZone;
	}
	@Override
	public String toString() {
		return String.format("ZoneChange{%s: %s-->%s}", this.getCard(), this.getFromCardZone(), this.getToCardZone());
	}
}
