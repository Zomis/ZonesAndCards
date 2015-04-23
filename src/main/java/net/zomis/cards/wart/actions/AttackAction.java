package net.zomis.cards.wart.actions;

import net.zomis.cards.model.StackAction;
import net.zomis.cards.wart.FightModule;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;

public class AttackAction extends StackAction {

	private HStoneCard target;
	private final HStoneCard source;
	private final HStoneGame game;

	public AttackAction(HStoneGame game, HStoneCard source, HStoneCard target) {
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
