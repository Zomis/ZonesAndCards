package net.zomis.cards.events.card;

import net.zomis.cards.events.CardEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;

public class CardPlayedEvent extends CardEvent {

	private final StackAction	action;

	public CardPlayedEvent(Card<?> card, StackAction action) {
		super(card);
		this.action = action;
	}
	
	public StackAction getAction() {
		return action;
	}

}
