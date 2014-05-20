package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneCardPlayedEvent;
import net.zomis.cards.hstone.events.HStoneDamagedEvent;
import net.zomis.cards.hstone.events.HStoneMinionDiesEvent;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;

public class ManaThreeCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 3,      NONE, 3, 5, "Laughing Sister").shroud().card());
		game.addCard(minion( 3,      FREE, 5, 1, "Magma Rager").card());
		game.addCard(minion( 3,      FREE, 2, 2, "Raid Leader").staticPT(anotherCard().and(allMinions()).and(samePlayer()), 1, 0).card());
		game.addCard(minion( 3,      FREE, 3, 1, "Wolfrider").charge().card());
		game.addCard(minion( 3,    COMMON, 1, 3, "Acolyte of Pain").on(HStoneDamagedEvent.class, drawCard(), thisCard()).card());
		game.addCard(minion( 3,    COMMON, 1, 4, "Dalaran Mage").spellDamage(1).card());
		game.addCard(minion( 3,    COMMON, 3, 3, "Earthen Ring Farseer").battlecry(heal(3)).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Flesheating Ghoul").on(HStoneMinionDiesEvent.class, selfPT(1, 0), allMinions().and(anotherCard())).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Harvest Golem").deathrattle(summon("Damaged Golem")).card());
		game.addCard(minion( 3,    COMMON, 2, 2, "Ironforge Rifleman").battlecry(damage(1)).card());
		game.addCard(minion( 3,    COMMON, 3, 3, "Ironfur Grizzly").taunt().card());
		game.addCard(minion( 3,    COMMON, 4, 2, "Jungle Panther").stealth().card());
		game.addCard(minion( 3,    COMMON, 3, 3, "Raging Worgen").staticPT(enrage(), 1, 0).staticAbility(enrage(), HSAbility.WINDFURY).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Razorfen Hunter").battlecry(summon("Boar")).card());
		game.addCard(minion( 3,    COMMON, 3, 1, "Scarlet Crusader").shield().card());
		game.addCard(minion( 3,    COMMON, 3, 2, "Shattered Sun Cleric").battlecry(to(allMinions().and(samePlayer()), otherPT(1, 1))).card());
		game.addCard(minion( 3,    COMMON, 1, 4, "Silverback Patriarch").taunt().card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Tauren Warrior").taunt().staticPT(enrage(), 3, 0).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Thrallmar Farseer").windfury().card());
//		game.addCard(minion( 3,      RARE, 0, 3, "Alarm-o-Bot").effect("At the start of your turn, swap this minion with a random one in your hand").card());
		game.addCard(minion( 3,      RARE, 4, 2, "Arcane Golem").charge().battlecry(giveOpponentManaCrystal()).card());
		game.addCard(minion( 3,      RARE, 2, 2, "Coldlight Oracle").battlecry(allDraw(2)).card());
		game.addCard(minion( 3,      RARE, 2, 3, "Coldlight Seer").battlecry(forEach(minionIsMurloc().and(not(thisCard())), null, otherPT(0, 2))).card());
		game.addCard(minion( 3,      RARE, 1, 4, "Demolisher").on(HStoneTurnStartEvent.class, toRandom(enemy(), damage(2)), samePlayer()).card());
//		game.addCard(minion( 3,      RARE, 2, 3, "Emperor Cobra").effect("Destroy any minion damaged by this minion").card());
		game.addCard(minion( 3,      RARE, 1, 5, "Imp Master").on(HStoneTurnEndEvent.class, combined(damageSelf(1), summon("Imp")), samePlayer()).card());
		game.addCard(minion( 3,      RARE, 4, 7, "Injured Blademaster").battlecry(damageSelf(4)).card());
//		game.addCard(minion( 3,      RARE, 3, 3, "Mind Control Tech").effect("<b>Battlecry:</b>").effect("If your opponent has 4 or more minions, take control of one at random").card());
		game.addCard(minion( 3,      RARE, 2, 2, "Questing Adventurer").on(HStoneCardPlayedEvent.class, selfPT(1, 1), samePlayer().and(not(thisCard()))).card());
		game.addCard(minion( 3,      EPIC, 4, 2, "Big Game Hunter").battlecry(to(allMinions().and(withAttackMore(7)), destroyTarget())).card());
		game.addCard(minion( 3,		 EPIC, 3, 3, "Blood Knight").battlecry(forEach(minionWithDivineShield(), selfPT(3, 3), removeShield())).card());
//		game.addCard(minion( 3,      EPIC, 3, 3, "Murloc Warleader").effect("ALL other Murlocs have +2/+1").card());
//		game.addCard(minion( 3,      EPIC, 3, 3, "Southsea Captain").effect("Your other Pirates have +1/+1").card());
		game.addCard(minion( 3, LEGENDARY, 5, 5, "King Mukla").battlecry(giveOpponentCard("Bananas", 2)).card());
//		game.addCard(minion( 3, LEGENDARY, 3, 3, "Tinkmaster Overspark").battlecry("Transform another random minion into a 5/5 Devilsaur or a 1/1 Squirrel").card());
	}

	private HStoneEffect giveOpponentCard(String string, int i) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO: giveOpponentCard
			}
		};
	}

	private HStoneEffect damageSelf(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				damage(damage).performEffect(source, source);
			}
		};
	}


	private HSFilter enemy() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return searcher.getPlayer() != target.getPlayer();
			}
		};
	}

}
