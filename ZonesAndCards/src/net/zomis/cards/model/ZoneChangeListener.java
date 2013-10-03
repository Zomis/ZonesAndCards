package net.zomis.cards.model;

import net.zomis.cards.events.ZoneChangeEvent;

public interface ZoneChangeListener {
//public interface ZoneChangeListener<TZone extends CardZone, TCard extends Card> {
	void onZoneChange(ZoneChangeEvent event);
}
