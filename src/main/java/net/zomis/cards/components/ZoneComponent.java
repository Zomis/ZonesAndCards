package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.iface.Component;
import net.zomis.cards.model.CardZone;

public class ZoneComponent implements Component {

	private final CardZone<CardWithComponents> zone;

	public ZoneComponent(FirstCompGame game, CardZone<CardWithComponents> zone) {
		this.zone = zone;
		game.addZone(zone);
	}
	
	public CardZone<CardWithComponents> getZone() {
		return zone;
	}
	
}
