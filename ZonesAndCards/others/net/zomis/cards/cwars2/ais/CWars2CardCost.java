package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class CWars2CardCost extends SubclassScorer<Player, StackAction, CWars2PlayAction> {

	public CWars2CardCost() {
		super(CWars2PlayAction.class);
	}
	
	@Override
	public double scoreSubclass(CWars2PlayAction cast, ScoreParameters<Player> scores) {
		CWars2Card card = (CWars2Card) cast.getCard().getModel();
		return CWars2ScorerNeeds.scoreMap(scores.getParameters(), card.getCosts());
	}

}
