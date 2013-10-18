package net.zomis.cards.model.ai;

import java.util.Random;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class RandomAI extends CardAI {

	public RandomAI(Random random) {
		this.setConfig(new ScoreConfigFactory<Player, StackAction>().withScorer(new RandomScorer<Player, StackAction>(random)).build());
	}

}
