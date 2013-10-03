package net.zomis.cards.model.actions;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.StackAction;

public class NextTurnAction extends StackAction {
	private CardGame game;
	public NextTurnAction(CardGame game) {
		this.game = game;
	}
	@Override
	public boolean isAllowed() {
		return this.game.isNextPhaseAllowed();
	}
	@Override
	protected void perform() {
		this.game.nextPhase();
	}
}
