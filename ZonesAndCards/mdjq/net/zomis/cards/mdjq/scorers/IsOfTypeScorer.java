package net.zomis.cards.mdjq.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.mdjq.MDJQCardModel;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.MDJQRes.TribalType;

public class IsOfTypeScorer extends AbstractScorer<MDJQPlayer, MDJQCardModel> {

	private Object type;

	public IsOfTypeScorer(CardType type) {
		this.type = type;
	}
	public IsOfTypeScorer(TribalType type) {
		this.type = type;
	}
	
	@Override
	public boolean workWith(ScoreParameters<MDJQPlayer> scores) {
		return true;
	}

	@Override
	public double getScoreFor(MDJQCardModel field, ScoreParameters<MDJQPlayer> scores) {
		return field.getTypes().contains(type) || field.getExtraTypes().contains(type) ? 1 : 0;
	}

}
