package net.zomis.cards.mdjq.events;

import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQZone;


public class MDJQZoneChangeEvent extends MDJQEvent {

	private MDJQZone toZone;
	private MDJQZone fromZone;
	private MDJQPermanent permanent;

	public MDJQZoneChangeEvent(MDJQPermanent permanent, MDJQZone from, MDJQZone to) {
		super((MDJQGame) permanent.getGame());
		this.fromZone = (MDJQZone) from;
		this.toZone = (MDJQZone) to;
		this.permanent = permanent;
	}
	
	public MDJQPermanent getPermanent() {
		return (MDJQPermanent) permanent;
	}

	public MDJQZone getFromZone() {
		return fromZone;
	}
	public MDJQZone getToZone() {
		return toZone;
	}
	
}
