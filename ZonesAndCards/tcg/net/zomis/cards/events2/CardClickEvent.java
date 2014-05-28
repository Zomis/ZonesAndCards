package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.model.StackAction;
import net.zomis.events.IEvent;

public class CardClickEvent implements IEvent {

	private final CardWithComponents<CompCardModel>	card;
	private StackAction	action;

	public CardClickEvent(CardWithComponents<CompCardModel> card) {
		this.card = card;
	}
	
	public CardWithComponents<CompCardModel> getCard() {
		return card;
	}
	
	public void setAction(StackAction action) {
		this.action = action;
	}
	
	public StackAction getAction() {
		return action;
	}

}
