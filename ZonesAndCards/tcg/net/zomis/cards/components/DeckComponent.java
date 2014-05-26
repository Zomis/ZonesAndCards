package net.zomis.cards.components;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events2.GameStartedEvent;
import net.zomis.cards.model.CardZone;

public class DeckComponent implements PlayerComponent {

	private final CardZone<CardWithComponents<CompCardModel>> deck;
	
	public DeckComponent(CompPlayer player) {
		this.deck = new CardZone<>("Deck", player);
		player.getGame().registerHandler(GameStartedEvent.class, this::onGameStart);
	}
	
	private void onGameStart(GameStartedEvent event) {
		FirstCompGame game = (FirstCompGame) event.getGame();
		game.addZone(deck);
	}
	
	public CardZone<CardWithComponents<CompCardModel>> getDeck() {
		return deck;
	}
	
}
