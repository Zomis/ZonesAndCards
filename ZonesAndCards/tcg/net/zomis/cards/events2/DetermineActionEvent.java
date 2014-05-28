package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.model.StackAction;
import net.zomis.events.IEvent;

public class DetermineActionEvent implements IEvent {

	private final CardWithComponents card;
	private StackAction	action;

	public DetermineActionEvent(CardWithComponents card) {
		this.card = card;
	}
	
	public CardWithComponents getCard() {
		return card;
	}
	
	public void setAction(StackAction action) {
		this.action = action;
	}
	
	public StackAction getAction() {
		return action;
	}

}
