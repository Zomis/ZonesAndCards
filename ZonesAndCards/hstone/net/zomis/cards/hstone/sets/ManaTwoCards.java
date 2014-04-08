package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.util.CardSet;

public class ManaTwoCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		HStoneGame hStoneGame = game;
		hStoneGame.addCard(minion(2, COMMON, 2, 3, "Amani Berserker").enrage(selfPT(3, 0)).card());
//		hStoneGame.addCard(minion(2, RARE  , 2, 3, "Sunfury Protector").battlecry(adjacents(Taunt)).card());
		
		hStoneGame.addCard(minion(2, BASE  , 2, 2, "Frostwolf Grunt").taunt().card());
		hStoneGame.addCard(minion(2, BASE  , 3, 2, "Bloodfen Raptor").card());
		hStoneGame.addCard(minion(2, BASE  , 2, 3, "River Crocolisk").card());
		hStoneGame.addCard(minion(2, RARE  , 4, 5, "Ancient Watcher").defense().card());
		hStoneGame.addCard(minion(2, BASE  , 3, 2, "Acidic Swamp Ooze").battlecry(destroyOppWeapon()).card());
		hStoneGame.addCard(minion(2, COMMON, 2, 1, "Bluegill Warrior").charge().card());
//		hStoneGame.addCard(minion(2, RARE  , 3, 2, "Wild Pyromancer").on(SpellPlayed, dealDamageAll(Minions, 1)).card());
//		hStoneGame.addCard(minion(2, COMMON, 3, 2, "Youthful Brewmaster").battlecry(unsummon(myMinion)).card());
//		hStoneGame.addCard(minion(2, COMMON, 1, 1, "Bloodmage Thalnos").spelldamage(1).death(DrawCard).card());
//		hStoneGame.addCard(minion(2, COMMON, 2, 3, "Bloodsail Raider").battlecry(self(Power += WeaponAttack)).card());
//		hStoneGame.addCard(minion(2, RARE  , 1, 1, "Captain's Parrot").battlecry(RandomPirateFromDeckToHand).card());
//		hStoneGame.addCard(minion(2, RARE  , 2, 2, "Crazed Alchemist").battlecry(SwapPTOnMinion).card());
//		hStoneGame.addCard(minion(2, COMMON, 2, 2, "Dire Wolf Alpha").staticEffect(AdjacentMinionsPT(+1, 0)).card());
//		hStoneGame.addCard(minion(2, RARE  , 0, 7, "Doomsayer").on(MyTurnStart, destroyAllMinions).card());
//		hStoneGame.addCard(minion(2, COMMON, 3, 2, "Faerie Dragon").shroudFrom(Spells, HeroPowers).card());
		hStoneGame.addCard(minion(2, COMMON, 2, 1, "Ironbeak Owl").battlecry(silencer()).card());
//		hStoneGame.addCard(minion(2, RARE  , 2, 1, "Knife Juggler").on(MeSummonMinion, dealDamageToRandomEnemy(1)).card());
//		hStoneGame.addCard(minion(2, COMMON, 2, 2, "Kobold Geomancer").spellDamage(1).card());
//		hStoneGame.addCard(minion(2, COMMON, 2, 1, "Loot Hoarder").death(DrawCard).card());
		
//		hStoneGame.addCard(minion(2, RARE  , 2, 1, "Lorewalker Cho").on(playerCastSpell, createCopyIntoOtherPlayerHand);
//		hStoneGame.addCard(minion(2, COMMON, 3, 2, "Mad Bomber").battlecry(damageSplitRandom(3, ALL)).card());
//		hStoneGame.addCard(minion(2, RARE  , 1, 3, "Mana Addict").on(iCastSpell, PT(+2, 0)).card());
//		hStoneGame.addCard(minion(2, RARE  , 2, 2, "Mana Wraith").staticEffect(cardCosts(Minions, +1)).card());
//		hStoneGame.addCard(minion(2, RARE  , 1, 3, "Master Swordsmith").on(myTurnEnd, randomFriendlyOther + 1 attack).card());
//		hStoneGame.addCard(minion(2, RARE  , 4, 4, "Millhouse Manastorm").battlecry(enemySpellsCost0nextTurn).charge().card());
		
		hStoneGame.addCard(minion(2, BASE  , 2, 1, "Murloc Tidehunter").battlecry(summon("Murloc Scout")).card());
		hStoneGame.addCard(minion(1, TOKEN , 1, 1, "Murloc Scout").card());
		hStoneGame.addCard(minion(2, BASE  , 1, 1, "Novice Engineer").battlecry(drawCard()).card());

//		hStoneGame.addCard(minion(2, RARE  , 2, 2, "Pint-Sized Summoner").staticEffect(cardCost(firstMinion, -1)).card());
		
//		hStoneGame.addCard(minion(2, RARE  , 0, 4, "Nat Pagle").on(myTurnStart, 50 percent chance draw card).card());
		
	}

}
