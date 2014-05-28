package net.zomis.cards.events2;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.events.IEvent;

public class DrawCardEvent extends CardEvent implements IEvent {

	private HasComponents player;

	public DrawCardEvent(HasComponents player, Card<?> card) {
		super(card);
		this.player = player;
	}
	
	public HasComponents getPlayer() {
		return player;
	}
}
