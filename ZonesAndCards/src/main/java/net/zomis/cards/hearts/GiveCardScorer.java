package net.zomis.cards.hearts;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;

public class GiveCardScorer extends AbstractScorer<Player, Card<?>> {

	@Override
	public boolean workWith(ScoreParameters<Player> scores) {
		return scores.getParameters().getGame().getCurrentPlayer() == null;
	}

	@Override
	public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
		return field.getCurrentZone().size() > 3 ? 1 : -1;
	}

}
