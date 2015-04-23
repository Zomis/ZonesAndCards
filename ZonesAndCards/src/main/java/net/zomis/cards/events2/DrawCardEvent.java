package net.zomis.cards.events2;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.events.CancellableEvent;
import net.zomis.events.IEvent;

public class DrawCardEvent extends CardEvent implements IEvent, CancellableEvent {

	private final HasComponents player;
	private boolean cancelled;

	public DrawCardEvent(CardGame<?, ?> game, HasComponents player, Card<?> card) {
		super(card, game);
		this.player = player;
	}
	
	public HasComponents getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
