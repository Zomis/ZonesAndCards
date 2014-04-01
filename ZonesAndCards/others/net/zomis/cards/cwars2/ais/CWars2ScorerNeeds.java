package net.zomis.cards.cwars2.ais;

import java.util.Map.Entry;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;

public class CWars2ScorerNeeds extends SubclassScorer<Player, StackAction, CWars2PlayAction> {

	public CWars2ScorerNeeds() {
		super(CWars2PlayAction.class);
	}

	@Override
	public double scoreSubclass(CWars2PlayAction cast, ScoreParameters<Player> scores) {
		CWars2Card card = (CWars2Card) cast.getCard().getModel();
		ResourceMap effects = card.getEffects();
		return scoreMap(scores.getParameters(), effects);
	}
	/**
	 * Costs: 			 Costs castle   = Costs   wall. Costs   wall = Almost  costs  castle.
	 * Effects: 		 Effects castle = Effects wall. Effects wall = Almost effects castle
	 * Opponent Effects: Effects castle = Effects wall. Effects wall = Almost effects castle
	 * 
	 * Me	4 castle, 8 wall.
	 * Opp	16 castle, 4 wall.
	 * 
	 * 
	 * 
	 * @param player
	 * @param effects
	 * @return
	 */
	public static double scoreMap(Player player, ResourceMap effects) {
		double result = 0;
		for (Entry<IResource, ResourceData> ee : effects.getData().entrySet()) {
			int effect = effects.getResources(ee.getKey());
			int current = player.getResources().getResources(ee.getKey());
			
			if (ee.getKey() == CWars2Res.WALL)
				current += effects.getResources(CWars2Res.CASTLE);
			
			current = Math.max(1, current);
			result = result + (double) effect / current;
		}
		return result;
	}

}
