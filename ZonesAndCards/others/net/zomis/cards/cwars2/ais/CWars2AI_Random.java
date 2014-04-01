package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.IsSubclassScorer;
import net.zomis.cards.cwars2.CWars2DiscardAction;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;

public class CWars2AI_Random extends CardAI {

	public CWars2AI_Random() {
		
		ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
		config.withScorer(new IsSubclassScorer<Player, StackAction>(CWars2PlayAction.class), 100);
		config.withScorer(new IsSubclassScorer<Player, StackAction>(CWars2DiscardAction.class), 10);
		config.withScorer(new IsSubclassScorer<Player, StackAction>(NextTurnAction.class), 1);
		
		this.setConfig(config);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
}
