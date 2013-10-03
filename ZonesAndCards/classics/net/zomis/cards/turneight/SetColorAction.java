package net.zomis.cards.turneight;

import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.StackAction;

public class SetColorAction extends StackAction {

	private final TurnEightGame	game;
	private final Suite suite;

	public SetColorAction(TurnEightGame game, SuiteModel model) {
		this(game, model.getSuite());
	}
	
	public SetColorAction(TurnEightGame game, Suite suite) {
		this.game = game;
		this.suite = suite;
	}

	public Suite getSuite() {
		return suite;
	}
	public TurnEightGame getGame() {
		return game;
	}
	
	@Override
	protected void perform() {
		this.game.setPlayerChoice(suite);
	}

}
