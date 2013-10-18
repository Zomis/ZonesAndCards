package test.net.zomis.cards;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.SubclassFixedScorer;
import net.zomis.cards.cwars2.CWars2DiscardAction;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;

public class CWars2AI extends CardAI {

	public CWars2AI() {
		
		ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
		config.withScorer(new SubclassFixedScorer<Player, StackAction, CWars2PlayAction>(CWars2PlayAction.class), 100);
		config.withScorer(new SubclassFixedScorer<Player, StackAction, CWars2DiscardAction>(CWars2DiscardAction.class), 10);
		config.withScorer(new SubclassFixedScorer<Player, StackAction, NextTurnAction>(NextTurnAction.class), 1);
		
		this.setConfig(config);
	}
	
}
