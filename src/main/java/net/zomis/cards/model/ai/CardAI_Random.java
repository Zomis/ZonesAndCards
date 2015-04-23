package net.zomis.cards.model.ai;

import java.util.Random;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;

public class CardAI_Random extends CardAI {

	private static Random random = new Random();
	public CardAI_Random() {
		this(random);
	}
	
	public CardAI_Random(Random random) {
		this.setConfig(new ScoreConfigFactory<Player, Card<?>>().withScorer(new RandomScorer<Player, Card<?>>(random)).build());
	}

}
