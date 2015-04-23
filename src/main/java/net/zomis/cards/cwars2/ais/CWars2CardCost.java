package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;

public class CWars2CardCost extends AbstractScorer<Player, Card<?>> {

	@Override
	public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
		CWars2Game game = (CWars2Game) field.getGame();
		if (game.isDiscardMode())
			return 0;
		CWars2Card card = (CWars2Card) field.getModel();
		return CWars2ScorerNeeds.scoreMap(scores.getParameters(), card.getCosts());
	}

}
