package net.zomis.cards.cwars2.cards;

import static net.zomis.cards.cwars2.CWars2Res.Resources.*;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.cwars2.RequiresTwo;
import net.zomis.cards.resources.IResource;

public class MultiplayerSpecificCards implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
		// Different in Multiplayer & Singleplayer:
		new CWars2CardFactory("Thief").setResourceCost(WEAPONS, 17).addAction(new ThiefAction(game, 8)).addTo(game);
		
		for (Producers val : Producers.values()) {
			IResource resource = val.getResource();
			new CWars2CardFactory("Sacrifice " + val).setResourceCost(val, 1).setResourceCost(resource, 6).setOppEffect(val, -1)
				.addAction(new RequiresTwo(game, val, 2)).addTo(game);
		}
		
		// Exists only in Multiplayer, needs to be bought:
		new CWars2CardFactory("Protect Resources").setResourceCost(WEAPONS, 8).addAction(new ProtectResourcesAction(game)).addTo(game);
		
		CWars2CardFactory reward = new CWars2CardFactory("Reward Workers");
		CWars2CardFactory remove = new CWars2CardFactory("Remove Resources").setResourceCost(CRYSTALS, 17);
		for (IResource res : Resources.values()) {
			reward.setResourceCost(res, 1);
			remove.setOppEffect(res, -8);
		}
		remove.addTo(game);
		reward.addAction(new MultiplyNextResourceIncome(game, 2, true));
		reward.addTo(game);
		
		new CWars2CardFactory("Hail Storm").setResourceCost(CRYSTALS, 14).setDamage(18).addTo(game);
		new CWars2CardFactory("Giant Snowball").setResourceCost(BRICKS, 13).setDamage(16).addTo(game);
		new CWars2CardFactory("Ram Attack").setResourceCost(BRICKS, 4).setResourceCost(WEAPONS, 4).setDamage(12).addTo(game);
	}

}
