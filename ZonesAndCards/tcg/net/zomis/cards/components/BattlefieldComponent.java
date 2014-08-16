package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;

public class BattlefieldComponent extends ZoneComponent {

	public BattlefieldComponent(CompPlayer player) {
		super(player.getGame(), new CardZone<CardWithComponents>("Battlefield", player));
	}
	
	public CardZone<CardWithComponents> getBattlefield() {
		return getZone();
	}
	
}
