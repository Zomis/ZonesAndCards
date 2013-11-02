package net.zomis.cards.events.game;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.StackAction;
import net.zomis.events.IEvent;

public class AfterActionEvent implements IEvent {

	private final CardGame game;
	private final StackAction action;

	public AfterActionEvent(CardGame game, StackAction action) {
		this.game = game;
		this.action = action;
	}
	
	public StackAction getAction() {
		return action;
	}
	public CardGame getGame() {
		return game;
	}
}
