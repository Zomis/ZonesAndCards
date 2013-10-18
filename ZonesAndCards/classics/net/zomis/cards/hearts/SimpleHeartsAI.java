package net.zomis.cards.hearts;

import java.util.Random;

import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.ParamAndField;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.RandomScorer;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;

public class SimpleHeartsAI extends CardAI {
	
	public SimpleHeartsAI(Random random) {
		ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>()
				.withScorer(new GiveCardScorer(), 1000)
				.withScorer(new RandomScorer<Player, StackAction>(random));
//				.withScorer(new IsAllowedScorer(), 10);
		this.setConfig(config.build());
		this.setMinScore(-800);
	}

	@Override
	public ParamAndField<Player, StackAction> play(Player player) {
		this.buffered = true;
		ParamAndField<Player, StackAction> sup = super.play(player);
//		CustomFacade.getLog().i("Super score: " + sup.getFieldScore().getScore() + " for action " + sup.getField());
		return sup;
	}
	
	@Override
	protected FieldScore<StackAction> nullAction(Player player) {
		if (player.getGame().getCurrentPlayer() == null) {
//			CustomFacade.getLog().i("Returning next turn action");
			return new FieldScore<StackAction>(new NextTurnAction(player.getGame()));
		}
		throw new AssertionError("There must be some available action for " + player);
	}
	
}
