package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.IsSubclassScorer;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.cwars2.CWars2DiscardAction;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.actions.ZoneMoveAction;
import net.zomis.cards.model.ai.CardAI;

public class CWars2AI_InstantWin extends CardAI {

	public CWars2AI_InstantWin() {
		ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
		config.withScorer(new IsSubclassScorer<Player, StackAction>(CWars2PlayAction.class), 100);
//		config.withScorer(new IsCardScorer("Archer"), 10000);
		config.withScorer(new CanWinScorer(), 100);
		config.withScorer(new IsSubclassScorer<Player, StackAction>(CWars2DiscardAction.class), 10);
		config.withScorer(new IsSubclassScorer<Player, StackAction>(NextTurnAction.class), 1);
		this.setConfig(config);
	}
	
	public static class IsCardScorer extends SubclassScorer<Player, StackAction, ZoneMoveAction> {

		private String	name;

		public IsCardScorer(String name) {
			super(CWars2PlayAction.class); // Don't change this or it will also apply to discard actions
			this.name = name;
		}

		@Override
		public double scoreSubclass(ZoneMoveAction cast, ScoreParameters<Player> scores) {
			return cast.getCard().getModel().getName().equals(name) ? 1 : 0;
		}
		
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
}
