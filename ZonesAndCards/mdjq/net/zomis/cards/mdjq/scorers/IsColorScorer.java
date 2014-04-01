package net.zomis.cards.mdjq.scorers;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.mdjq.MDJQCardModel;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.mdjq.MDJQRes.MColor;

public class IsColorScorer extends MDJQScorer {

	private final MColor	color;

	public IsColorScorer(MColor color) {
		this.color = color;
	}
	
	@Override
	public double getScoreFor(MDJQCardModel field, ScoreParameters<MDJQPlayer> scores) {
		return field.getManaCost().getResources(color) > 0 ? 1 : 0;
	}

}
