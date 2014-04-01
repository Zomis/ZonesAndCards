package net.zomis.cards.cwars2.cards;

import static net.zomis.cards.cwars2.CWars2Res.Resources.*;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.RequiresTwo;

public class SinglePlayerCards implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
//		new CWars2CardFactory("Spy").setResourceCost(weapons, 4).addTo(cards);
//		new CWars2CardFactory("Sabotage").setResourceCost(weapons, 10).addTo(cards);
		
		new CWars2CardFactory("Thief16").setResourceCost(WEAPONS, 16).addAction(new ThiefAction(game, 8)).addTo(game);
		
		for (Producers val : Producers.values()) {
			new CWars2CardFactory("SuperSacrifice " + val).setResourceCost(val, 1).setOppEffect(val, -2)
				.addAction(new RequiresTwo(game, val, 2)).addTo(game);
		}
		
	}

}
