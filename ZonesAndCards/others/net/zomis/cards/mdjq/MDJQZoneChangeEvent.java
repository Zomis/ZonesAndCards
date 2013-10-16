package net.zomis.cards.mdjq;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;


public class MDJQZoneChangeEvent extends MDJQEvent {

	private MDJQZone toZone;
	private MDJQZone fromZone;

	public MDJQZoneChangeEvent(Card permanent, CardZone from, CardZone to) {
		super((MDJQGame) permanent.getGame());
		this.fromZone = (MDJQZone) from;
		this.toZone = (MDJQZone) to;
	}

	public MDJQZone getFromZone() {
		return fromZone;
	}
	public MDJQZone getToZone() {
		return toZone;
	}
	
}
