package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.RPSComponent;
import net.zomis.fight.ext.WinResult;

public class RPSCardsSystem implements GameSystem {

	public enum RPS {
		ROCK, PAPER, SCISSORS;
	}
	
	public static WinResult fight(RPS one, RPS other) {
		if (one == RPS.ROCK)
			return WinResult.resultFor(other, RPS.SCISSORS, one);
		if (one == RPS.SCISSORS)
			return WinResult.resultFor(other, RPS.PAPER, one);
		if (one == RPS.PAPER)
			return WinResult.resultFor(other, RPS.ROCK, one);
		return null;
	}
	
	public RPSCardsSystem(FirstCompGame game) {
		for (RPS rps : RPS.values()) {
			game.addCard(new CompCardModel(rps.name()).addComponent(new RPSComponent(rps)));
		}
	}

	@Override
	public void onStart(FirstCompGame game) {
	}

//	private CardEffect setPicked(RPS rps) {
//		return src -> src.getOwner().getComponent(ChosenCardComponent.class).setChosenAction();
//	}

}
