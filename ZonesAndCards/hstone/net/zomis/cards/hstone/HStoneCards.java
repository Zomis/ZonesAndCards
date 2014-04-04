package net.zomis.cards.hstone;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneMinionType.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.factory.HStoneEffect;

public class HStoneCards {

	public static void neutralSmall(HStoneGame hStoneGame) {
		hStoneGame.addCard(minion(0, COMMON, 1, 1, "Wisp").card());
//		hStoneGame.addCard(minion(2, RARE  , 2, 3, "Sunfury Protector").battlecry(adjacents(Taunt)).card());
		
//		hStoneGame.addCard(minion(2, RARE  , 3, 2, "Wild Pyromancer").on(SpellPlayed, dealDamageAll(Minions, 1)).card());
//		hStoneGame.addCard(minion(2, COMMON, 3, 2, "Youthful Brewmaster").battlecry(unsummon(myMinion)).card());
		
//		hStoneGame.addCard(minion(3, COMMON, 1, 3, "Acolyte of Pain").on(takesDamage, drawCard()).card());
		
		hStoneGame.addCard(minion(1, BASE  , 1, 1, "Elven Archer").battlecry(damage(1)).card());
		hStoneGame.addCard(minion(1, BASE  , 2, 1, "Murloc Raider").is(MURLOC).card());
		hStoneGame.addCard(minion(2, BASE  , 2, 2, "Frostwolf Grunt").taunt().card());
		hStoneGame.addCard(minion(1, COMMON, 1, 1, "Argent Squire").shield().card());
		hStoneGame.addCard(minion(1, BASE  , 1, 2, "Goldshire Footman").taunt().card());
		hStoneGame.addCard(minion(1, COMMON, 0, 4, "Shieldbearer").taunt().card());
		hStoneGame.addCard(minion(2, BASE  , 3, 2, "Bloodfen Raptor").card());
		hStoneGame.addCard(minion(2, BASE  , 2, 3, "River Crocolisk").card());
		hStoneGame.addCard(minion(1, BASE  , 1, 1, "Stonetusk Boar").charge().card());
		hStoneGame.addCard(minion(1, BASE  , 2, 1, "Voodoo Doctor").battlecry(heal(2)).card());
		hStoneGame.addCard(minion(1, COMMON, 2, 1, "Worgen Infiltrator").stealth().card());
		hStoneGame.addCard(minion(1, COMMON, 1, 1, "Young Dragonhawk").windfury().card());
		hStoneGame.addCard(minion(2, RARE  , 4, 5, "Ancient Watcher").defense().card());
		hStoneGame.addCard(minion(2, BASE  , 3, 2, "Acidic Swamp Ooze").battlecry(destroyOppWeapon()).card());
		hStoneGame.addCard(minion(1, COMMON, 2, 1, "Abusive Sergeant").battlecry(tempBoost(2, 0)).card());
		hStoneGame.addCard(minion(1, RARE  , 1, 1, "Angry Chicken").enrage(selfPT(5, 0)).card());
		hStoneGame.addCard(minion(1, RARE  , 1, 2, "Bloodsail Corsair").battlecry(removeDurabilityOppWeapon(1)).card());
		hStoneGame.addCard(minion(2, COMMON, 2, 1, "Bluegill Warrior").charge().card());
		hStoneGame.addCard(minion(1, BASE  , 1, 1, "Grimscale Oracle").staticEffect(typePTBonus(MURLOC, 1, 0)).card());
//		hStoneGame.addCard(minion(3, EPIC  , 3, 3, "Blood Knight").battlecry(forEach(minionWithDivineShield(), removeShieldAnd  myPT(3, 3))).card());
		
		hStoneGame.addCard(minion(3, RARE  , 2, 2, "Coldlight Oracle").battlecry(allDraw(2)).card());
//		hStoneGame.addCard(minion(3, RARE  , 2, 3, "Coldlight Seer").battlecry(forEach(minionIsMurloc(), otherPT(0, 2))).card());
		hStoneGame.addCard(minion(3, BASE  , 1, 4, "Dalaran Mage").spellDamage(1).card());
		
//		hStoneGame.addCard(minion(3, RARE  , 1, 4, "Demolisher").on(myStartTurn, damageToRandom(enemy, 2).card());
		hStoneGame.addCard(minion(3, COMMON, 3, 3, "Earthen Ring Farseer").battlecry(heal(3)).card());
//		hStoneGame.addCard(minion(3, RARE  , 2, 3, "Emperor Cobra").on(thisDealsDamage(), destroyThatOne()).card());
//		hStoneGame.addCard(minion(3, COMMON, 2, 3, "Flesheating Ghoul").on(minionDies(), myPT(1, 0)).card());
		

//		hStoneGame.addCard(minion(3, RARE  , 0, 3, "Alarm-o-Bot").on(startTurn, exchange(thisCard, randomMinionHand())).card());
		hStoneGame.addCard(minion(3, RARE  , 4, 2, "Arcane Golem").charge().battlecry(giveOpponentManaCrystal()).card());
//		hStoneGame.addCard(minion(3, RARE  , 4, 2, "Big Game Hunter").battlecry(destroyMinion(targetHasPT(7, 0))).card());
		
		hStoneGame.addCard(minion(2, COMMON, 2, 3, "Amani Berserker").enrage(selfPT(3, 0)).card());
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
		
		
//		hStoneGame.addCard(minion(1, RARE  , 2, 1, "Young Priestess").on(EndTurn, randomTarget().health(+1)).card());
		
		
//		hStoneGame.addCard(minion(1, COMMON, 2, 1, "Southsea Deckhand").staticEffect(chargeIfHasWeapon()).card());
//		hStoneGame.addCard(minion(1, RARE  , 1, 2, "Secretkeeper").on(SecretPlayed, PT(1, 1)).card());
//		hStoneGame.addCard(minion(1, RARE  , 1, 2, "Murloc Tidecaller").on(Summoned(Murloc), PT(1, 0)).card());
//		hStoneGame.addCard(minion(1, RARE  , 1, 2, "Hungry Crab").battlecry(destroy(isOfType(HStoneMinionType.MURLOC))).card());
//		hStoneGame.addCard(minion(1, COMMON, 2, 1, "Leper Gnome").death(damageTo(2, Target.enemyHero())).card());
//		hStoneGame.addCard(minion(1, RARE, 1, 2, "Lightwarden").on(Heal, Effect(triggerCard, PT(2, 0))).card());
		
		
		hStoneGame.addCard(spell(1, COMMON, "Ice Lance").effect(freezeOrDamage(4)).card());
		
		// TODO: Page 45 and forward
	}

	private static HStoneEffect allDraw(final int cardCount) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				for (int i = 0; i < cardCount; i++) {
					source.getPlayer().drawCard();
					source.getPlayer().getNextPlayer().drawCard();
				}
			}
		};
	}

	private static HStoneEffect giveOpponentManaCrystal() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneTarget source, HStoneTarget target) {
				source.getPlayer().getNextPlayer().getResources().changeResources(HStoneRes.MANA_TOTAL, 1);
			}
		};
	}
	
}
