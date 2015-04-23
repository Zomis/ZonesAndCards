package net.zomis.cards.events.zone;

import net.zomis.cards.events.ZoneEvent;
import net.zomis.cards.model.CardZone;

public class ZoneReverseEvent extends ZoneEvent {

	public ZoneReverseEvent(CardZone<?> zone) {
		super(zone);
	}

}
