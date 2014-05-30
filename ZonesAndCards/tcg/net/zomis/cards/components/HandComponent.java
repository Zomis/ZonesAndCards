package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;

public class HandComponent extends ZoneComponent {

	public HandComponent(CompPlayer player) {
		super(player.getGame(), new CardZone<CardWithComponents>("Hand", player));
		this.getZone().setKnown(player, true);
	}
	
	public CardZone<CardWithComponents> getHand() {
		return getZone();
	}
	
}
