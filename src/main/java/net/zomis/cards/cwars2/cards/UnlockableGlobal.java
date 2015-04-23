package net.zomis.cards.cwars2.cards;

import static net.zomis.cards.cwars2.CWars2Res.Resources.*;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Resources;

public class UnlockableGlobal implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
		new CWars2CardFactory("Dragon").setResourceCost(BRICKS, 20).setResourceCost(CRYSTALS, 20).setDamage(38).addTo(game);
		new CWars2CardFactory("Trojan Horse").setResourceCost(WEAPONS, 20).setResourceCost(BRICKS, 15).setCastleDamage(30).addTo(game);
		new CWars2CardFactory("Comet Strike").setResourceCost(CRYSTALS, 10).setResourceCost(BRICKS, 20).setDamage(30).addTo(game);
		
		CWars2CardFactory curse = new CWars2CardFactory("Curse");
		for (Resources res : Resources.values()) {
			curse.setResourceCost(res, 15);
			curse.setOppEffect(res, -1);
			curse.setOppEffect(res.getProducer(), -1);
		}
		curse.setOppEffect(CWars2Res.WALL, -1);
		curse.setOppEffect(CWars2Res.CASTLE, -1);
		curse.addTo(game);
	}

}
