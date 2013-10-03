package net.zomis.cards.turneight;

import net.zomis.cards.model.StackAction;

public class DrawCardAction extends StackAction {
	private final TurnEightGame game;
	public DrawCardAction(TurnEightGame game) {
		this.game = game;
	}
	@Override
	public boolean isAllowed() {
		return !this.game.hasPlayed();
	}
	@Override
	protected void perform() {
		this.game.playerDrawFromDeck(game.getCurrentPlayer());
	}
}
