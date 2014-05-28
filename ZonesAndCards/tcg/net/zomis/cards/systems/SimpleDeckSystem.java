package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.events2.GameStartedEvent;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckList;

public class SimpleDeckSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(GameStartedEvent.class, this::onStart);
	}
	
	private void onStart(GameStartedEvent event) {
		FirstCompGame game = (FirstCompGame) event.getGame();
		for (CompPlayer pl : game.getPlayers()) {
			if (!pl.hasComponent(DeckSourceComponent.class))
				return;
			DeckList deck = new DeckList("Deck").add(52, "Random Card");
			DeckBuilder.createExact(pl.getComponent(DeckSourceComponent.class), deck.getCount(game));
		}
	}

}
