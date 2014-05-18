package net.zomis.cards.hstone.factory;

import net.zomis.cards.util.DeckList;

public class HStoneDecks {

	public static DeckList testDeck() {
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
			.add(1, "Acidic Swamp Ooze")
			.add(1, "Ancient Watcher")
			.add(1, "Blood Knight")
			.add(1, "Novice Engineer")
			.add(1, "Murloc Tidehunter")
			.add(1, "Bloodfen Raptor")
			.add(1, "Bluegill Warrior")
			.add(1, "Arcane Golem")
			.add(1, "Frostwolf Grunt")
			.add(1, "Ice Lance");
	}

	public static DeckList shamanDeck() {
		return DeckList.newInstance().addMany("Earth Shock;2 Forked Lightning;2 Frost Shock;Stormforged Axe;Acidic Swamp Ooze;2 Flametongue Totem;Knife Juggler;Mana Juggler;Mana Addict;" +
				"Feral Spirit;2 Hex;2 Lightning Storm;Flesheating Ghoul;Mana Tide Totem;2 Unbound Elemental;Ogre Magi;2 Sin'jin Shieldmasta;Spellbreaker;Bloodlust;Earth Elemental;" +
				"Gadgetzan Auctioneer;Stranglethorn Tiger;2 Fire Elemental");
	}
	
}
