package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.FightModule;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneTarget;
import net.zomis.cards.model.StackAction;

public class AttackAction extends StackAction {

	private HStoneTarget	target;
	private final HStoneTarget	source;
	private final HStoneGame	game;

	public AttackAction(HStoneGame game, HStoneTarget source, HStoneTarget target) {
		this.source = source;
		this.target = target;
		this.game = game;
	}
	
	@Override
	public boolean actionIsAllowed() {
		return true;
	}
	
	@Override
	protected void onPerform() {
		FightModule.attack(game, source, target);
	}

}
