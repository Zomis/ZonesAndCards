package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.ResourceMap;

public class CWars2OpponentNeeds extends SubclassScorer<Player, StackAction, CWars2PlayAction> {

	public CWars2OpponentNeeds() {
		super(CWars2PlayAction.class);
	}
	
	@Override
	public double scoreSubclass(CWars2PlayAction cast, ScoreParameters<Player> scores) {
		CWars2Card card = (CWars2Card) cast.getCard().getModel();
		ResourceMap effects = card.getOpponentEffects();
		return CWars2ScorerNeeds.scoreMap(scores.getParameters().getOpponents().get(0), effects);
		
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
	}

}
