package net.zomis.cards.events;

import net.zomis.cards.model.CardGame;
import net.zomis.events.IEvent;

public class CardGameEvent implements IEvent {

	private final CardGame<?, ?> game;

	public CardGameEvent(CardGame<?, ?> game) {
		this.game = game;
	}
	
	public final CardGame<?, ?> getGame() {
		return game;
	}
	
}
