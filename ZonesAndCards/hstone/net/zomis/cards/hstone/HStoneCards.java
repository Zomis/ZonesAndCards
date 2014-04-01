package net.zomis.cards.hstone;

import net.zomis.cards.hstone.factory.Battlecry;
import net.zomis.cards.hstone.factory.HStoneCardFactory;
import net.zomis.cards.hstone.factory.HStoneMinionType;
import net.zomis.cards.hstone.factory.HStoneRarity;

public class HStoneCards {

	public static void neutralSmall(HStoneGame hStoneGame) {
		hStoneGame.addCard(HStoneCardFactory.minion(0, HStoneRarity.COMMON, 1, 1, "Wisp").card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.BASE  , 1, 1, "Elven Archer").battlecry(Battlecry.damage(1)).card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.BASE  , 2, 1, "Murloc Raider").is(HStoneMinionType.MURLOC).card());
		hStoneGame.addCard(HStoneCardFactory.minion(2, HStoneRarity.BASE  , 2, 2, "Frostwolf Grunt").taunt().card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.COMMON, 1, 1, "Argent Squire").shield().card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.BASE  , 1, 2, "Goldshire Footman").taunt().card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.COMMON, 0, 4, "Shieldbearer").taunt().card());
		hStoneGame.addCard(HStoneCardFactory.minion(2, HStoneRarity.BASE  , 3, 2, "Bloodfen Raptor").card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.BASE  , 1, 1, "Stonetusk Boar").charge().card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.BASE  , 2, 1, "Voodoo Doctor").battlecry(Battlecry.heal(2)).card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		hStoneGame.addCard(HStoneCardFactory.minion(1, HStoneRarity.COMMON, 1, 1, "Young Dragonhawk").windfury().card());
		hStoneGame.addCard(HStoneCardFactory.minion(2, HStoneRarity.RARE  , 4, 5, "Ancient Watcher").defense().card());
		hStoneGame.addCard(HStoneCardFactory.minion(2, HStoneRarity.BASE  , 3, 2, "Acidic Swamp Ooze").battlecry(Battlecry.destroyOppWeapon()).card());
		hStoneGame.addCard(HStoneCardFactory.minion(2, HStoneRarity.COMMON, 2, 1, "Bluegill Warrior").charge().card());
		
		hStoneGame.addCard(HStoneCardFactory.spell(1, HStoneRarity.COMMON, "Ice Lance").effect(Battlecry.freezeOrDamage(4)).card());
	}
	
}
