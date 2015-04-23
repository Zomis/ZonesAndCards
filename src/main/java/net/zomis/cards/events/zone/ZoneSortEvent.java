package net.zomis.cards.events.zone;

import net.zomis.cards.events.ZoneEvent;
import net.zomis.cards.model.CardZone;

public class ZoneSortEvent extends ZoneEvent {

	public ZoneSortEvent(CardZone<?> zone) {
		super(zone);
	}

}
