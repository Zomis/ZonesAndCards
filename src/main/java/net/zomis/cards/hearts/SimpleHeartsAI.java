package net.zomis.cards.hearts;

import java.util.Random;

import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.CardAI;

public class SimpleHeartsAI extends CardAI {
	
	public SimpleHeartsAI(Random random) {
		ScoreConfigFactory<Player, Card<?>> config = new ScoreConfigFactory<Player, Card<?>>()
				.withScorer(new GiveCardScorer(), 1000)
				.withScorer(new RandomScorer<Player, Card<?>>(random));
//				.withScorer(new IsAllowedScorer(), 10);
		this.setConfig(config.build());
		this.setMinScore(-800);
	}

	@Override
	public ParamAndField<Player, Card<?>> play(Player player) {
		this.buffered = true;
		ParamAndField<Player, Card<?>> sup = super.play(player);
//		CustomFacade.getLog().i("Super score: " + sup.getFieldScore().getScore() + " for action " + sup.getField());
		return sup;
	}
	
	@Override
	protected FieldScore<Card<?>> nullAction(Player player) {
		if (player.getGame().getCurrentPlayer() == null) {
//			CustomFacade.getLog().i("Returning next turn action");
			HeartsGame game = (HeartsGame) player.getGame();
			return new FieldScore<Card<?>>(game.getNextTurnCard());
		}
		throw new AssertionError("There must be some available action for " + player);
	}
	
}
