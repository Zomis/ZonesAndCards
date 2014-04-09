package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.PreScorer;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.ResourceMap;

public class CWars2ScorerResourceDemand implements PreScorer<Player> {

	@Override
	public Object analyze(Player params) {
		return new AnalyzeResult(params).analyze();
	}

	@Override
	public void onScoringComplete() {
		
	}
	
	public static class AnalyzeResult {
		private Player	player;
		private ResourceMap map;
		
		public AnalyzeResult(Player params) {
			this.player = params;
			this.map = new ResourceMap();
		}

		private AnalyzeResult analyze() {
			CWars2Player pl = (CWars2Player) player;
			for (Card<CWars2Card> card : pl.getHand()) {
				map.change(card.getModel().getCosts(), 1);
			}
			return this;
		}
		public ResourceMap getMap() {
			return map;
		}
	}

}
