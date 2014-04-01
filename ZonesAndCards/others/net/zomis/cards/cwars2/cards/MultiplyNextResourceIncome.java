package net.zomis.cards.cwars2.cards;

import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.resources.ResourceStrategy;

public class MultiplyNextResourceIncome extends PublicAction implements ResourceStrategy {
	
	private CWars2Game game;
	private int	multiplier;
	private boolean	currentPlayer;

	MultiplyNextResourceIncome() {}
	public MultiplyNextResourceIncome(CWars2Game game, int multiplier, boolean currentPlayer) {
		this.game = game;
		this.multiplier = multiplier;
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void onPerform() {
		ResourceMap res = currentPlayer ? game.getCurrentPlayer().getResources() : ((CWars2Player) game.getCurrentPlayer().getOpponents().get(0)).getResources();
		for (Producers prod : Producers.values()) {
			res.setResourceStrategy(prod, this);
		}
	}

	@Override
	public int getResourceAmount(ResourceData type, ResourceMap map) {
		int value = new ResourceMap(map).setResourceStrategy(type.getResource(), null).getResources(type.getResource());
		return value * multiplier;
	}
}

