package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.scorers.SubclassScorer;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;

public class CanWinScorer extends SubclassScorer<Player, StackAction, CWars2PlayAction> {

	public CanWinScorer() {
		super(CWars2PlayAction.class);
	}

	@Override
	public double scoreSubclass(CWars2PlayAction cast, ScoreParameters<Player> scores) {
		CWars2Player player = (CWars2Player) scores.getParameters();
		CWars2Player opponent = player.getNextPlayer();
		CWars2Card card = (CWars2Card) cast.getCard().getModel();
		if (card.getEffects().getResources(CWars2Res.CASTLE) + player.getResources().getResources(CWars2Res.CASTLE) >= 100)
			return 1;
		if (opponent.getResources().getResources(CWars2Res.CASTLE) - card.castleDamage() <= 0)
			return 1;
		if (opponent.getResources().getResources(CWars2Res.CASTLE) +
				opponent.getResources().getResources(CWars2Res.WALL) - card.damage() - card.castleDamage() <= 0)
			return 1;
		
		return 0;
	}
	
}
