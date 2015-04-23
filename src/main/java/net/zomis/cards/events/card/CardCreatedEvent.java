package net.zomis.cards.events.card;

import net.zomis.cards.events.CardInZoneEvent;
import net.zomis.cards.model.Card;
import net.zomis.events.IEvent;

public class CardCreatedEvent extends CardInZoneEvent implements IEvent {

	public CardCreatedEvent(final Card<?> card) {
		super(card);
	}
}
