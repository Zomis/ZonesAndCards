package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.FirstCompGame;

public class RandomCardModelSystem implements GameSystem {

	@Override
	public void onStart(FirstCompGame game) {
		game.addCard(new CompCardModel("Random Card"));
	}

}
