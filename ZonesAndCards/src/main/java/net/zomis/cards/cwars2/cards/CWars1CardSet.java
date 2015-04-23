package net.zomis.cards.cwars2.cards;

import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;

public class CWars1CardSet implements CWarsCardSet {

	@Override
	public void addCards(CWars2Game game) {
		new CWars2CardFactory("Wall").setResourceCost(Resources.BRICKS, 1).setMyEffect(CWars2Res.WALL, 3).addTo(game);
		new CWars2CardFactory("Base").setResourceCost(Resources.BRICKS, 1).setMyEffect(CWars2Res.WALL, 2).addTo(game);
		new CWars2CardFactory("Defence").setResourceCost(Resources.BRICKS, 3).setMyEffect(CWars2Res.WALL, 6).addTo(game);
		new CWars2CardFactory("Reserve").setResourceCost(Resources.BRICKS, 3).setResourceCost(CWars2Res.WALL, 4).setMyEffect(CWars2Res.CASTLE, 8).addTo(game);
		new CWars2CardFactory("Tower").setResourceCost(Resources.BRICKS, 5).setMyEffect(CWars2Res.CASTLE, 5).addTo(game);
		new CWars2CardFactory("School").setResourceCost(Resources.BRICKS, 8).setMyEffect(Producers.BUILDERS, 1).addTo(game);
		new CWars2CardFactory("Wain").setResourceCost(Resources.BRICKS, 10).setMyEffect(CWars2Res.CASTLE, 8).setOppEffect(CWars2Res.CASTLE, -4).addTo(game);
		new CWars2CardFactory("Fence").setResourceCost(Resources.BRICKS, 12).setMyEffect(CWars2Res.WALL, 22).addTo(game);
		new CWars2CardFactory("Fort").setResourceCost(Resources.BRICKS, 18).setMyEffect(CWars2Res.CASTLE, 20).addTo(game);
		new CWars2CardFactory("Babylon").setResourceCost(Resources.BRICKS, 39).setMyEffect(CWars2Res.CASTLE, 32).addTo(game);
		
		new CWars2CardFactory("Archer").setResourceCost(Resources.WEAPONS, 1).setOppEffect(CWars2Res.WALL, -2).addTo(game);
		new CWars2CardFactory("Knight").setResourceCost(Resources.WEAPONS, 2).setOppEffect(CWars2Res.WALL, -3).addTo(game);
		new CWars2CardFactory("Rider").setResourceCost(Resources.WEAPONS, 2).setOppEffect(CWars2Res.WALL, -4).addTo(game);
		new CWars2CardFactory("Platoon").setResourceCost(Resources.WEAPONS, 4).setOppEffect(CWars2Res.WALL, -6).addTo(game);
		new CWars2CardFactory("Recruit").setResourceCost(Resources.WEAPONS, 8).setMyEffect(Producers.RECRUITS, 1).addTo(game);
		new CWars2CardFactory("Attack").setResourceCost(Resources.WEAPONS, 10).setOppEffect(CWars2Res.WALL, -12).addTo(game);
		
		CWars2CardFactory saboteur = new CWars2CardFactory("Saboteur").setResourceCost(Resources.WEAPONS, 12);
		for (Resources res : Resources.values()) {
			saboteur.setOppEffect(res, -4);
		}
		saboteur.addTo(game);
		
		new CWars2CardFactory("Thief").setResourceCost(Resources.WEAPONS, 15).addAction(new ThiefAction(game, 5)).addTo(game);
		new CWars2CardFactory("Swat").setResourceCost(Resources.WEAPONS, 18).setOppEffect(CWars2Res.CASTLE, -10).addTo(game);
		new CWars2CardFactory("Banshee").setResourceCost(Resources.WEAPONS, 28).setOppEffect(CWars2Res.WALL, -32).addTo(game);
		
		// TODO: 2 producers 5 resources 30 castle 10 wall, discard 1. 70 cards in deck.
		for (Resources res : Resources.values()) {
			new CWars2CardFactory("Conjure " + res.toString()).setResourceCost(Resources.CRYSTALS, 4).setMyEffect(res, 8).addTo(game);
			new CWars2CardFactory("Crush " + res.toString()).setResourceCost(Resources.CRYSTALS, 4).setOppEffect(res, -8).addTo(game);
		}
		
		new CWars2CardFactory("Sorcerer").setResourceCost(Resources.CRYSTALS, 8).setMyEffect(Producers.WIZARDS, 1).addTo(game);
		new CWars2CardFactory("Dragon").setResourceCost(Resources.CRYSTALS, 21).setOppEffect(CWars2Res.WALL, -25).addTo(game);
		new CWars2CardFactory("Pixies").setResourceCost(Resources.CRYSTALS, 22).setMyEffect(CWars2Res.CASTLE, 22).addTo(game);
		
		CWars2CardFactory curse = new CWars2CardFactory("Curse");
		curse.setResourceCost(Resources.CRYSTALS, 45);
		for (Resources res : Resources.values()) {
			curse.setOppEffect(res, -1);
			curse.setOppEffect(res.getProducer(), -1);
			curse.setMyEffect(res, 1);
			curse.setMyEffect(res.getProducer(), 1);
		}
		curse.setMyEffect(CWars2Res.WALL, 1);
		curse.setMyEffect(CWars2Res.CASTLE, 1);
		curse.setOppEffect(CWars2Res.WALL, -1);
		curse.setOppEffect(CWars2Res.CASTLE, -1);
		curse.addTo(game);
		
		CWars2CardFactory university = new CWars2CardFactory("University").setResourceCost(Resources.BRICKS, 26);
		for (Producers prod : Producers.values())
			university.setMyEffect(prod, 1);
		university.addTo(game);
		
		stealify(Producers.WIZARDS, new CWars2CardFactory("Enchant mage").setResourceCost(Resources.CRYSTALS, 20)).addTo(game);
		stealify(Producers.RECRUITS, new CWars2CardFactory("Bribe").setResourceCost(Resources.WEAPONS, 20)).addTo(game);
		stealify(Producers.BUILDERS, new CWars2CardFactory("Abduct builder").setResourceCost(Resources.BRICKS, 20)).addTo(game);
		
		CWars2CardFactory poison = new CWars2CardFactory("Poison").setResourceCost(Resources.WEAPONS, 36);
		for (Producers prod : Producers.values())
			poison.setOppEffect(prod, -1);
		poison.addTo(game);
		
//		new CWars2CardFactory("Additional Cardslot 1").addTo(game); // TODO: Additional Cardslot here is not a card, it's a fixed bonus.
//		new CWars2CardFactory("Additional Cardslot 2").addTo(game);
	}

	private CWars2CardFactory stealify(Producers producer,
			CWars2CardFactory factory) {
		factory.setMyEffect(producer, 1);
		factory.setOppEffect(producer, -1);
		return factory;
	}

}
