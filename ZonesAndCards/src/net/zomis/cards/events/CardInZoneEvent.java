package net.zomis.cards.events;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

public class CardInZoneEvent extends CardEvent {

	private final CardZone	zone;

	public CardInZoneEvent(Card card) {
		super(card);
		this.zone = card.getCurrentZone();
	}
	
	public final CardZone getZone() {
		return zone;
	}

}
