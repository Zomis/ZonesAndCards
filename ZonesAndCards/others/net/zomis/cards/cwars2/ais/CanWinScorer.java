package net.zomis.cards.cwars2.ais;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.cwars2.CWars2Card;
import net.zomis.cards.cwars2.CWars2Player;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;

public class CanWinScorer extends AbstractScorer<Player, Card<?>> {

	@Override
	public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
		CWars2Player player = (CWars2Player) scores.getParameters();
		CWars2Player opponent = player.getNextPlayer();
		CWars2Card card = (CWars2Card) field.getModel();
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
