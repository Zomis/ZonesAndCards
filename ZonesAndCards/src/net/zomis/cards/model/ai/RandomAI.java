package net.zomis.cards.model.ai;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class RandomAI extends CardAI {

	public RandomAI(CardGame game) {
		super(game);
		
		this.setConfig(new ScoreConfigFactory<Player, StackAction>().withScorer(new RandomScorer<Player, StackAction>(game.getRandom())).build());
		
	}

}
