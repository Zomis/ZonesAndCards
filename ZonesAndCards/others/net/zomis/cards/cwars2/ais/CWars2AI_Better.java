package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.IsSubclassScorer;
import net.zomis.cards.cwars2.CWars2DiscardAction;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;

public class CWars2AI_Better extends CardAI {

	public CWars2AI_Better() {
		
		ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
		config.withPreScorer(new CWars2ScorerResourceDemand());
//		config.withScorer(new ResourceMapScorer(new ScoreOpponentEffects()));
		// TODO: Scorer for when to really focus on decreasing a high opponent castle. Current scorers would give very low score for that since it does not have much impact.
		config.withScorer(new CWars2CardCost(), -1); // Negative resources for me = Bad.
		config.withScorer(new CWars2ScorerNeeds());
		config.withScorer(new CWars2OpponentNeeds(), -1); // What is bad for my opponent is good for me
		
		config.withScorer(new CanWinScorer(), 100); // If it's possible to instantly win, then it's a really good idea.
		config.withScorer(new IsSubclassScorer<Player, StackAction>(CWars2PlayAction.class), 100);
		config.withScorer(new IsSubclassScorer<Player, StackAction>(CWars2DiscardAction.class), 10);
		config.withScorer(new IsSubclassScorer<Player, StackAction>(NextTurnAction.class), 9);
		
		this.setConfig(config);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
}
