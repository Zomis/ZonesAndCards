package net.zomis.cards.cwars2.cards;

import static net.zomis.cards.cwars2.CWars2Res.Producers.*;
import static net.zomis.cards.cwars2.CWars2Res.Resources.*;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;

public class BricksCards implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
		new CWars2CardFactory("Builder").setResourceCost(BRICKS, 8).setMyEffect(BUILDERS, 1).addTo(game);
		new CWars2CardFactory("Tower").setResourceCost(BRICKS, 10).setMyEffect(CWars2Res.CASTLE, 10).addTo(game);
		new CWars2CardFactory("Tavern").setResourceCost(BRICKS, 12).setMyEffect(CWars2Res.CASTLE, 15).addTo(game);
		new CWars2CardFactory("House").setResourceCost(BRICKS, 5).setMyEffect(CWars2Res.CASTLE, 5).addTo(game);
		new CWars2CardFactory("Babylon").setResourceCost(BRICKS, 25).setMyEffect(CWars2Res.CASTLE, 30).addTo(game);
		new CWars2CardFactory("Wall").setResourceCost(BRICKS, 4).setMyEffect(CWars2Res.WALL, 6).addTo(game);
		new CWars2CardFactory("School").setResourceCost(BRICKS, 30).setMyEffect(BUILDERS, 1)
			.setMyEffect(RECRUITS, 1).setMyEffect(WIZARDS, 1).addTo(game);
		
		new CWars2CardFactory("Fence").setResourceCost(BRICKS, 5).setMyEffect(CWars2Res.WALL, 9).addTo(game);
		new CWars2CardFactory("Catapult").setResourceCost(BRICKS, 10).setDamage(12).addTo(game);
		new CWars2CardFactory("Battering Ram").setResourceCost(BRICKS, 7).setDamage(9).addTo(game);
		new CWars2CardFactory("Wain").setResourceCost(BRICKS, 10).setOppEffect(CWars2Res.CASTLE, -6).setMyEffect(CWars2Res.CASTLE, 6).addTo(game);
		new CWars2CardFactory("All Bricks").setResourceCost(BRICKS, 1).addAction(new AllResourcesFocusOn(game, BRICKS)).addTo(game);
		new CWars2CardFactory("Large Wall").setResourceCost(BRICKS, 14).setMyEffect(CWars2Res.WALL, 20).addTo(game);
		new CWars2CardFactory("Reverse").setResourceCost(BRICKS, 3).setResourceCost(CWars2Res.WALL, 4).setMyEffect(CWars2Res.CASTLE, 8).addTo(game);
	}

}
