package net.zomis.cards.model;

public class CardZoneLocation {

	private final CardZone<?> zone;
	private final int index;
	
	private static final int BOTTOM = Integer.MAX_VALUE;
	private static final int TOP = -1;
	
	public CardZoneLocation(CardZone<?> zone, int index) {
		this.zone = zone;
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public CardZone<?> getZone() {
		return zone;
	}

	public boolean isTop() {
		return this.index == TOP;
	}
	
	public boolean isBottom() {
		return this.index == BOTTOM;
	}

	public static CardZoneLocation nowhere() {
		return new CardZoneLocation(null, BOTTOM);
	}
	
	public static CardZoneLocation bottomOf(CardZone<?> zone) {
		return new CardZoneLocation(zone, BOTTOM);
	}
	
	public static CardZoneLocation topOf(CardZone<?> zone) {
		return new CardZoneLocation(zone, TOP);
	}

	public static CardZoneLocation indexIn(CardZone<?> zone, int index) {
		return new CardZoneLocation(zone, index);
	}

}
