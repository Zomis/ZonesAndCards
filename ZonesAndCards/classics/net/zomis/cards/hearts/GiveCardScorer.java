package net.zomis.cards.hearts;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class GiveCardScorer extends SubclassScorer<Player, StackAction, HeartsGiveAction> {

	public GiveCardScorer() {
		super(HeartsGiveAction.class);
	}

	@Override
	public boolean workWith(ScoreParameters<Player> scores) {
		return scores.getParameters().getGame().getCurrentPlayer() == null;
	}

	@Override
	public double scoreSubclass(HeartsGiveAction cast, ScoreParameters<Player> scores) {
		return cast.getCard().getCurrentZone().size() > 3 ? 1 : -1;
	}

}
