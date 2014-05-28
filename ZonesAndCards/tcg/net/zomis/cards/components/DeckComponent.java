package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;

public class DeckComponent implements PlayerComponent {

	private final CardZone<CardWithComponents<CompCardModel>> deck;
	
	public DeckComponent(CompPlayer player) {
		this.deck = new CardZone<>("Deck", player);
		player.getGame().addZone(deck);
	}
	
	public CardZone<CardWithComponents<CompCardModel>> getDeck() {
		return deck;
	}
	
}
