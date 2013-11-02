package net.zomis.cards.cwars2;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassFixedScorer;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;

public class CWars2AI_InstantWin extends CardAI {

	public CWars2AI_InstantWin() {
		ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
		config.withScorer(new SubclassFixedScorer<Player, StackAction, CWars2PlayAction>(CWars2PlayAction.class), 100);
		config.withScorer(new IsCardScorer("Archer"), 10000);
		config.withScorer(new CanWinScorer(), 100);
		config.withScorer(new SubclassFixedScorer<Player, StackAction, CWars2DiscardAction>(CWars2DiscardAction.class), 10);
		config.withScorer(new SubclassFixedScorer<Player, StackAction, NextTurnAction>(NextTurnAction.class), 1);
		this.setConfig(config);
	}
	
	private static class IsCardScorer extends SubclassScorer<Player, StackAction, CWars2PlayAction> {

		private String	name;

		public IsCardScorer(String name) {
			super(CWars2PlayAction.class);
			this.name = name;
		}

		@Override
		public double scoreSubclass(CWars2PlayAction cast, ScoreParameters<Player> scores) {
			return cast.getCard().getModel().getName().equals(name) ? 1 : 0;
		}
		
	}
	private static class CanWinScorer extends SubclassScorer<Player, StackAction, CWars2PlayAction> {

		public CanWinScorer() {
			super(CWars2PlayAction.class);
		}

		@Override
		public double scoreSubclass(CWars2PlayAction cast, ScoreParameters<Player> scores) {
			CWars2Player player = (CWars2Player) scores.getParameters();
			CWars2Player opponent = player.getNextPlayer();
			CWars2Card card = (CWars2Card) cast.getCard().getModel();
			if (card.effects.getResources(CWars2Res.CASTLE) + player.getResources().getResources(CWars2Res.CASTLE) >= 100)
				return 1;
			if (opponent.getResources().getResources(CWars2Res.CASTLE) - card.castleDamage() <= 0)
				return 1;
			if (opponent.getResources().getResources(CWars2Res.CASTLE) +
					opponent.getResources().getResources(CWars2Res.WALL) - card.damage() - card.castleDamage() <= 0)
				return 1;
			
			return 0;
		}
		
	}
	
}
