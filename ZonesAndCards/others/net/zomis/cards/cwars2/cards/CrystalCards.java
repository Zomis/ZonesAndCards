package net.zomis.cards.cwars2.cards;

import static net.zomis.cards.cwars2.CWars2Res.Producers.*;
import static net.zomis.cards.cwars2.CWars2Res.Resources.*;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.resources.IResource;

public class CrystalCards implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
		new CWars2CardFactory("Mage").setResourceCost(CRYSTALS, 8).setMyEffect(WIZARDS, 1).addTo(game);
		new CWars2CardFactory("Lightning").setResourceCost(CRYSTALS, 20).setDamage(22).addTo(game);
		new CWars2CardFactory("Quake").setResourceCost(CRYSTALS, 24).setDamage(27).addTo(game);
		new CWars2CardFactory("Pixies").setResourceCost(CRYSTALS, 18).setMyEffect(CWars2Res.CASTLE, 22).addTo(game);
		new CWars2CardFactory("Magic Wall").setResourceCost(CRYSTALS, 14).setMyEffect(CWars2Res.WALL, 20).addTo(game);
		new CWars2CardFactory("Magic Defense").setResourceCost(CRYSTALS, 10).addAction(new MagicAttackMultiply(game, false, 0)).addTo(game);
		new CWars2CardFactory("Magic Weapons").setResourceCost(CRYSTALS, 15).addAction(new MagicAttackMultiply(game, true, 2)).addTo(game);
		
		for (IResource res : Resources.values()) {
			new CWars2CardFactory("Add " + res).setResourceCost(CRYSTALS, 5).setMyEffect(res, 8).addTo(game);
			new CWars2CardFactory("Remove " + res).setResourceCost(CRYSTALS, 5).setOppEffect(res, -8).addTo(game);
		}
		
		new CWars2CardFactory("All Crystals").setResourceCost(CRYSTALS, 1).addAction(new AllResourcesFocusOn(game, CRYSTALS)).addTo(game);
	}

}
