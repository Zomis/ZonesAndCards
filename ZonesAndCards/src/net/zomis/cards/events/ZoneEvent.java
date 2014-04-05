package net.zomis.cards.events;

import net.zomis.cards.model.CardZone;

public class ZoneEvent extends CardGameEvent {

	private final CardZone<?>	zone;

	public ZoneEvent(CardZone<?> zone) {
		super(zone.getGame());
		this.zone = zone;
	}
	
	public final CardZone<?> getZone() {
		return zone;
	}

}
