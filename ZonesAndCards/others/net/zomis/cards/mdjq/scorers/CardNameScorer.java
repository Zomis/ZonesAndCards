package net.zomis.cards.mdjq.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.mdjq.MDJQCardModel;
import net.zomis.cards.mdjq.MDJQPlayer;

public class CardNameScorer extends AbstractScorer<MDJQPlayer, MDJQCardModel> {

	private final String	name;

	public CardNameScorer(String name) {
		this.name = name;
	}
	
	@Override
	public boolean workWith(ScoreParameters<MDJQPlayer> scores) {
		return true;
	}

	@Override
	public double getScoreFor(MDJQCardModel field, ScoreParameters<MDJQPlayer> scores) {
		return field.getName().contentEquals(name) ? 1 : 0;
	}

}
