package net.zomis.cards.events2;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.events.IEvent;

public class DrawCardEvent extends CardEvent implements IEvent {

	private final HasComponents player;

	public DrawCardEvent(CardGame<?, ?> game, HasComponents player, Card<?> card) {
		super(card, game);
		this.player = player;
	}
	
	public HasComponents getPlayer() {
		return player;
	}
}
