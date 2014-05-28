package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;

public class DeckComponent implements PlayerComponent {

	private final CardZone<CardWithComponents> deck;
	
	public DeckComponent(CompPlayer player) {
		this.deck = new CardZone<>("Deck", player);
		player.getGame().addZone(deck);
	}
	
	public CardZone<CardWithComponents> getDeck() {
		return deck;
	}
	
}
