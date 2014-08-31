package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;

public class SpecialZoneComponent extends ZoneComponent {

	public SpecialZoneComponent(CompPlayer player) {
		super(player.getGame(), new CardZone<CardWithComponents>("Special", player));
		this.getZone().setGloballyKnown(true);
	}
	
	public CardZone<CardWithComponents> getSpecialZone() {
		return getZone();
	}
	
}
