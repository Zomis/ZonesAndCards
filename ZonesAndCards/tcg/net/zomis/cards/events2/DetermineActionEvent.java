package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.model.StackAction;

public class DetermineActionEvent extends CompCard1Event {

	private StackAction	action;

	public DetermineActionEvent(CardWithComponents card) {
		super(card);
	}
	
	public void setAction(StackAction action) {
		this.action = action;
	}
	
	public StackAction getAction() {
		return action;
	}

	public boolean hasAction() {
		return action != null;
	}

}
