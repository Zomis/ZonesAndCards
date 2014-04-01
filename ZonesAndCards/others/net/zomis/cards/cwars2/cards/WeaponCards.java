package net.zomis.cards.cwars2.cards;

import static net.zomis.cards.cwars2.CWars2Res.Producers.*;
import static net.zomis.cards.cwars2.CWars2Res.Resources.*;
import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;

public class WeaponCards implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
		new CWars2CardFactory("Archer").setResourceCost(WEAPONS, 1).setDamage(2).addTo(game);
		new CWars2CardFactory("Fire Archer").setResourceCost(WEAPONS, 3).setDamage(5).addTo(game);
		new CWars2CardFactory("Bomb").setResourceCost(WEAPONS, 14).setDamage(18).addTo(game);
		new CWars2CardFactory("Cannon").setResourceCost(WEAPONS, 16).setDamage(20).addTo(game);
		new CWars2CardFactory("Platoon").setResourceCost(WEAPONS, 7).setDamage(9).addTo(game);
		new CWars2CardFactory("Ambush").setResourceCost(WEAPONS, 20).setCastleDamage(15).addTo(game);

		new CWars2CardFactory("Knight").setResourceCost(WEAPONS, 10).setDamage(10).addTo(game);
		new CWars2CardFactory("Recruit").setResourceCost(WEAPONS, 8).setMyEffect(RECRUITS, 1).addTo(game);
		new CWars2CardFactory("Guards").setResourceCost(WEAPONS, 7).setMyEffect(CWars2Res.WALL, 12).addTo(game);
		new CWars2CardFactory("Roadblock").setResourceCost(WEAPONS, 8).addAction(new MultiplyNextResourceIncome(game, 0, false)).addTo(game);
		new CWars2CardFactory("All Weapons").setResourceCost(WEAPONS, 1).addAction(new AllResourcesFocusOn(game, WEAPONS)).addTo(game);
	}

}
