package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;

public class DeckComponent extends ZoneComponent {

	public DeckComponent(CompPlayer player) {
		super(player.getGame(), new CardZone<CardWithComponents>("Deck", player));
	}
	
	public CardZone<CardWithComponents> getDeck() {
		return getZone();
	}
	
}
