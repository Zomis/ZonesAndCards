package net.zomis.cards.cwars2;

import net.zomis.ArrayIterator;
import net.zomis.IndexIterator;
import net.zomis.IndexIteratorStatus;
import net.zomis.cards.util.ResourceType;

public class CWars2CardSet {
	
	private CWars2Game	game;

	public CWars2CardSet() {
		
	}
	
	public void addCards(CWars2Game game) {
		this.game = game;
		addWeaponCards();
		addCrystalCards();
		addBrickCards();
		addUnlockableCards();
		addBuyableCards();
	}
	
	private void addBuyableCards() {
//		new CWars2CardFactory("Protect Resources").setResourceCost(weapons, 8).addTo(cards);
//		
//		CWars2CardFactory reward = new CWars2CardFactory("Reward Workers");
//		CWars2CardFactory remove = new CWars2CardFactory("Remove Resources").setResourceCost(crystals, 17);
//		for (ResourceType res : restypes) {
//			reward.setResourceCost(res, 1);
//			remove.setResourceEffect(res, -8);
//		}
//		reward.addTo(cards);
//		remove.addTo(cards);
		
		new CWars2CardFactory("Hail Storm").setResourceCost(game.crystals, 14).setDamage(18).addTo(game);
		new CWars2CardFactory("Giant Snowball").setResourceCost(game.bricks, 13).setDamage(16).addTo(game);
		new CWars2CardFactory("Ram Attack").setResourceCost(game.bricks, 4).setResourceCost(game.weapons, 4).setDamage(12).addTo(game);
		
	}

	private void addUnlockableCards() {
		for (IndexIteratorStatus<ResourceType> res : new IndexIterator<ResourceType>(new ArrayIterator<ResourceType>(game.producers))) {
			int index = res.getIndex();
			ResourceType val = res.getValue();
			ResourceType resource = game.restypes[index];
			new CWars2CardFactory("Sacrifice " + val.getName()).setResourceCost(val, 1).setResourceCost(resource, 6)
				.setOppEffect(val, -1)
				.addAction(new RequiresTwo(game, val, 2)).addTo(game);
		}
		
		new CWars2CardFactory("Dragon").setResourceCost(game.bricks, 20).setResourceCost(game.crystals, 20).setDamage(38).addTo(game);
		new CWars2CardFactory("Trojan Horse").setResourceCost(game.weapons, 20).setResourceCost(game.bricks, 15).setCastleDamage(30).addTo(game);
		new CWars2CardFactory("Comet Strike").setResourceCost(game.crystals, 10).setResourceCost(game.bricks, 20).setDamage(30).addTo(game);
		
		CWars2CardFactory curse = new CWars2CardFactory("Curse");
		for (int i = 0; i < this.game.restypes.length; i++) {
			curse.setResourceCost(this.game.restypes[i], 15);
			curse.setOppEffect(this.game.restypes[i], -1);
			curse.setOppEffect(this.game.producers[i], -1);
		}
		curse.setOppEffect(game.wall, -1);
		curse.setOppEffect(game.castle, -1);
		curse.addTo(game);
	}

	private void addBrickCards() {
		new CWars2CardFactory("Builder").setResourceCost(game.bricks, 8).setMyEffect(game.builders, 1).addTo(game);
		new CWars2CardFactory("Tower").setResourceCost(game.bricks, 10).setMyEffect(game.castle, 10).addTo(game);
		new CWars2CardFactory("Tavern").setResourceCost(game.bricks, 12).setMyEffect(game.castle, 15).addTo(game);
		new CWars2CardFactory("House").setResourceCost(game.bricks, 5).setMyEffect(game.castle, 5).addTo(game);
		new CWars2CardFactory("Babylon").setResourceCost(game.bricks, 25).setMyEffect(game.castle, 30).addTo(game);
		new CWars2CardFactory("Wall").setResourceCost(game.bricks, 4).setMyEffect(game.wall, 6).addTo(game);
		new CWars2CardFactory("School").setResourceCost(game.bricks, 30).setMyEffect(game.builders, 1)
			.setMyEffect(game.recruits, 1).setMyEffect(game.wizards, 1).addTo(game);
		
		new CWars2CardFactory("Fence").setResourceCost(game.bricks, 5).setMyEffect(game.wall, 9).addTo(game);
		new CWars2CardFactory("Catapult").setResourceCost(game.bricks, 10).setDamage(12).addTo(game);
		new CWars2CardFactory("Battering Ram").setResourceCost(game.bricks, 7).setDamage(9).addTo(game);
		new CWars2CardFactory("Wain").setResourceCost(game.bricks, 10).setOppEffect(game.castle, -6).setMyEffect(game.castle, 6).addTo(game);
//		new CWars2CardFactory("All Bricks").setResourceCost(bricks, 1).addTo(cards);
		new CWars2CardFactory("Large Wall").setResourceCost(game.bricks, 14).setMyEffect(game.wall, 20).addTo(game);
		new CWars2CardFactory("Reverse").setResourceCost(game.bricks, 3).setResourceCost(game.wall, 4).setMyEffect(game.castle, 8).addTo(game);
	}

	private void addCrystalCards() {
		new CWars2CardFactory("Mage").setResourceCost(game.crystals, 8).setMyEffect(game.wizards, 1).addTo(game);
		new CWars2CardFactory("Lightning").setResourceCost(game.crystals, 20).setDamage(22).addTo(game);
		new CWars2CardFactory("Quake").setResourceCost(game.crystals, 24).setDamage(27).addTo(game);
		new CWars2CardFactory("Pixies").setResourceCost(game.crystals, 18).setMyEffect(game.castle, 22).addTo(game);
		new CWars2CardFactory("Magic Wall").setResourceCost(game.crystals, 14).setMyEffect(game.wall, 20).addTo(game);
//		new CWars2CardFactory("Magic Defense").setResourceCost(crystals, 10).addTo(cards);
//		new CWars2CardFactory("Magic Weapons").setResourceCost(crystals, 15).addTo(cards);
		
		for (ResourceType res : this.game.restypes) {
			new CWars2CardFactory("Add " + res.getName()).setResourceCost(game.crystals, 5).setMyEffect(res, 8).addTo(game);
			new CWars2CardFactory("Remove " + res.getName()).setResourceCost(game.crystals, 5).setOppEffect(res, -8).addTo(game);
		}
		
//		new CWars2CardFactory("All Crystals").setResourceCost(crystals, 1).addTo(cards);
	}

	private void addWeaponCards() {
		new CWars2CardFactory("Archer").setResourceCost(game.weapons, 1).setDamage(2).addTo(game);
		new CWars2CardFactory("Fire Archer").setResourceCost(game.weapons, 3).setDamage(5).addTo(game);
		new CWars2CardFactory("Bomb").setResourceCost(game.weapons, 14).setDamage(18).addTo(game);
		new CWars2CardFactory("Cannon").setResourceCost(game.weapons, 16).setDamage(20).addTo(game);
//		new CWars2CardFactory("Spy").setResourceCost(weapons, 8).addTo(cards);
		new CWars2CardFactory("Platoon").setResourceCost(game.weapons, 7).setDamage(9).addTo(game);
		new CWars2CardFactory("Ambush").setResourceCost(game.weapons, 20).setCastleDamage(15).addTo(game);

		new CWars2CardFactory("Knight").setResourceCost(game.weapons, 10).setDamage(10).addTo(game);
//		new CWars2CardFactory("Thief").setResourceCost(weapons, 17).addTo(cards);
		new CWars2CardFactory("Recruit").setResourceCost(game.weapons, 8).setMyEffect(game.recruits, 1).addTo(game);
		new CWars2CardFactory("Guards").setResourceCost(game.weapons, 7).setMyEffect(game.wall, 12).addTo(game);
//		new CWars2CardFactory("Roadblock").setResourceCost(weapons, 8).addTo(cards);
//		new CWars2CardFactory("All Weapons").setResourceCost(weapons, 1).addTo(cards);
//		new CWars2CardFactory("Sabotage").setResourceCost(weapons, 13).addTo(cards);
	}

}
