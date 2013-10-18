package net.zomis.cards.events;

import net.zomis.cards.model.CardGame;
import net.zomis.events.CancellableEvent;
import net.zomis.events.IEvent;

public class GameOverEvent implements IEvent, CancellableEvent {

	private CardGame	game;
	private boolean	cancelled;

	public GameOverEvent(CardGame cardGame) {
		this.game = cardGame;
	}
	
	public CardGame getGame() {
		return game;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}
