package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.cwars2.CWars2DiscardAction;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.ai.IsActionClass;

public class CWars2AI_Random extends CardAI {

	public CWars2AI_Random() {
		
		ScoreConfigFactory<Player, Card<?>> config = new ScoreConfigFactory<Player, Card<?>>();
		config.withScorer(new IsActionClass(CWars2PlayAction.class), 100);
		config.withScorer(new IsActionClass(CWars2DiscardAction.class), 10);
		config.withScorer(new IsActionClass(NextTurnAction.class), 1);
		
		this.setConfig(config);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
}
