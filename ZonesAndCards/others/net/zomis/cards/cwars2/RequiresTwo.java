package net.zomis.cards.cwars2;

import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.ResourceType;

public class RequiresTwo extends StackAction {
	private CWars2Game	game;
	private ResourceType	res;
	private int	amount;

	public RequiresTwo(CWars2Game cWars2Game, ResourceType val, int amount) {
		this.game = cWars2Game;
		this.res = val;
		this.amount = amount;
	}
	
	@Override
	public boolean isAllowed() {
		return game.getCurrentPlayer().getResources().hasResources(res, amount);
	}

}
