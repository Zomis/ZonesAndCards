package net.zomis.cards.events2;

import net.zomis.cards.events.CardGameEvent;
import net.zomis.cards.model.CardGame;

public class GameStartedEvent extends CardGameEvent {

	public GameStartedEvent(CardGame<?, ?> game) {
		super(game);
	}

}
