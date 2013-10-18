package net.zomis.cards.cwars2;

import net.zomis.cards.model.StackAction;

public class ToggleDiscardAction extends StackAction {

	private CWars2Game	game;

	public ToggleDiscardAction(CWars2Game game) {
		this.game = game;
	}
	
	@Override
	public boolean isAllowed() {
		return game.getDiscarded() == 0;
	}
	
	@Override
	protected void onPerform() {
		game.toggleDiscardMode();
	}

}
