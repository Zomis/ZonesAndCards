package net.zomis.cards.events.zone;

import net.zomis.cards.events.ZoneEvent;
import net.zomis.cards.model.CardZone;

public class ZoneShuffleEvent extends ZoneEvent {

	public ZoneShuffleEvent(CardZone<?> zone) {
		super(zone);
	}

}
