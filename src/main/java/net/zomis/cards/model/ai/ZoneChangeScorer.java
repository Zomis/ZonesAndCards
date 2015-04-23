package net.zomis.cards.model.ai;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.ZoneMoveAction;

@Deprecated
public class ZoneChangeScorer extends SubclassScorer<Player, StackAction, ZoneMoveAction> {

	@Deprecated
	public ZoneChangeScorer() {
		super(ZoneMoveAction.class);
		// TODO: Scorer to check ZoneMoveActions, fromZone, toZone, getCard->getName... I think it could come in handy.
	}

	@Override
	public double scoreSubclass(ZoneMoveAction cast, ScoreParameters<Player> scores) {
		return 0;
	}
	
	

}
