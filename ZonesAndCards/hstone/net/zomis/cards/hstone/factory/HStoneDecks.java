package net.zomis.cards.hstone.factory;

import net.zomis.cards.util.DeckList;

public class HStoneDecks {

	public static DeckList mageDefault() {
		return DeckList.newInstance("First Test Deck")
			.add(2, "Argent Squire")
			.add(2, "Wisp")
			.add(2, "Goldshire Footman")
			.add(2, "Elven Archer")
			.add(2, "Murloc Raider")
			.add(2, "Shieldbearer")
			.add(2, "Stonetusk Boar")
			.add(2, "Voodoo Doctor")
			.add(2, "Worgen Infiltrator")
			.add(2, "Young Dragonhawk")
			.add(2, "Acidic Swamp Ooze")
			.add(1, "Ancient Watcher")
			.add(1, "Novice Engineer")
			.add(1, "Murloc Tidehunter")
			.add(1, "Bloodfen Raptor")
			.add(1, "Bluegill Warrior")
			.add(1, "Arcane Golem")
			.add(1, "Frostwolf Grunt")
			.add(1, "Ice Lance");
	}

}
