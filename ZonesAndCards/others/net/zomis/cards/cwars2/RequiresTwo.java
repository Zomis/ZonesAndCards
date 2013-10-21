package net.zomis.cards.cwars2;

import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.IResource;

public class RequiresTwo extends StackAction {
	private CWars2Game	game;
	private IResource	res;
	private int	amount;

	public RequiresTwo(CWars2Game cWars2Game, IResource val, int amount) {
		this.game = cWars2Game;
		this.res = val;
		this.amount = amount;
	}
	
	@Override
	public boolean actionIsAllowed() {
		return game.getCurrentPlayer().getResources().hasResources(res, amount);
	}

}
