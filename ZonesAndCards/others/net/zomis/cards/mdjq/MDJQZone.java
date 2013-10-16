package net.zomis.cards.mdjq;

import net.zomis.cards.model.CardZone;

public class MDJQZone extends CardZone {

	public static enum ZoneType {
		LIBRARY, HAND, BATTLEFIELD, GRAVEYARD, EXILE, TEMPORARY;
	}
	
	private ZoneType	zoneType;
	private MDJQPlayer	owner;
	
	public MDJQZone(String zoneName, ZoneType type, MDJQPlayer owner) {
		super(zoneName);
		this.zoneType = type;
		this.owner = owner;
	}
	
	public MDJQPlayer getZoneOwner() {
		return owner;
	}
	
	@Override
	public MDJQGame getGame() {
		return (MDJQGame) super.getGame();
	}

	public boolean isHand() {
		return zoneType == ZoneType.HAND;
	}

	
}
