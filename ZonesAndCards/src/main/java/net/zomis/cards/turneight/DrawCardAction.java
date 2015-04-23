package net.zomis.cards.turneight;

import net.zomis.cards.model.StackAction;

public class DrawCardAction extends StackAction {
	private final TurnEightGame game;
	public DrawCardAction(TurnEightGame game) {
		this.game = game;
	}
	@Override
	public boolean actionIsAllowed() {
		return !this.game.hasPlayed();
	}
	@Override
	protected void onPerform() {
		this.game.playerDrawFromDeck(game.getCurrentPlayer());
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "--" + game.getCurrentPlayer();
	}
}
