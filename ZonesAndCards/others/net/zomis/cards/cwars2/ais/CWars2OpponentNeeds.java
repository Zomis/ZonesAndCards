package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.ResourceMap;

public class CWars2OpponentNeeds extends AbstractScorer<Player, Card<?>> {

//		double result = 0;
//		for (Entry<IResource, ResourceData> ee : effects.getData().entrySet()) {
//			int effect = effects.getResources(ee.getKey());
//			int current = scores.getParameters().getResources().getResources(ee.getKey());
//			
//			if (ee.getKey() == CWars2Res.WALL)
//				current += effects.getResources(CWars2Res.CASTLE);
//			
//			current = Math.max(1, current);
//			result = result + (double) effect / current;
//		}
//		
//		return result;

	@Override
	public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
		CWars2Game game = (CWars2Game) field.getGame();
		if (game.isDiscardMode())
			return 0;
		
		CWars2Card card = (CWars2Card) field.getModel();
		ResourceMap effects = card.getOpponentEffects();
		return CWars2ScorerNeeds.scoreMap(scores.getParameters().getOpponents().get(0), effects);
	}

}
