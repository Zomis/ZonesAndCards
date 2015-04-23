package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStoneDamagedEvent;
import net.zomis.cards.wart.events.HStoneMinionDiesEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HStoneEffect;

public class ManaThreeCards implements CardSet<HStoneGame> {

	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 3,      NONE, 3, 5, "Laughing Sister").shroud().card());
		game.addCard(minion( 3,      FREE, 5, 1, "Magma Rager").card());
		game.addCard(minion( 3,      FREE, 2, 2, "Raid Leader").staticPT(f.anotherCard().and(f.allMinions()).and(f.samePlayer()), 1, 0).card());
		game.addCard(minion( 3,      FREE, 3, 1, "Wolfrider").charge().card());
		game.addCard(minion( 3,    COMMON, 1, 3, "Acolyte of Pain").on(HStoneDamagedEvent.class, e.drawCard(), f.thisCard()).card());
		game.addCard(minion( 3,    COMMON, 1, 4, "Dalaran Mage").spellDamage(1).card());
		game.addCard(minion( 3,    COMMON, 3, 3, "Earthen Ring Farseer").battlecry(e.heal(3)).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Flesheating Ghoul").on(HStoneMinionDiesEvent.class, e.selfPT(1, 0), f.allMinions().and(f.anotherCard())).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Harvest Golem").deathrattle(e.summon("Damaged Golem")).card());
		game.addCard(minion( 3,    COMMON, 2, 2, "Ironforge Rifleman").battlecry(e.damage(1)).card());
		game.addCard(minion( 3,    COMMON, 3, 3, "Ironfur Grizzly").taunt().card());
		game.addCard(minion( 3,    COMMON, 4, 2, "Jungle Panther").stealth().card());
		game.addCard(minion( 3,    COMMON, 3, 3, "Raging Worgen").staticPT(f.enrage(), 1, 0).staticAbility(f.enrage(), HSAbility.WINDFURY).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Razorfen Hunter").battlecry(e.summon("Boar")).card());
		game.addCard(minion( 3,    COMMON, 3, 1, "Scarlet Crusader").shield().card());
		game.addCard(minion( 3,    COMMON, 3, 2, "Shattered Sun Cleric").battlecry(e.to(f.allMinions().and(f.samePlayer()), e.otherPT(1, 1))).card());
		game.addCard(minion( 3,    COMMON, 1, 4, "Silverback Patriarch").taunt().card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Tauren Warrior").taunt().staticPT(f.enrage(), 3, 0).card());
		game.addCard(minion( 3,    COMMON, 2, 3, "Thrallmar Farseer").windfury().card());
//		game.addCard(minion( 3,      RARE, 0, 3, "Alarm-o-Bot").effect("At the start of your turn, swap this minion with a random one in your hand").card());
		game.addCard(minion( 3,      RARE, 4, 2, "Arcane Golem").charge().battlecry(e.giveOpponentManaCrystal()).card());
		game.addCard(minion( 3,      RARE, 2, 2, "Coldlight Oracle").battlecry(e.allDraw(2)).card());
		game.addCard(minion( 3,      RARE, 2, 3, "Coldlight Seer").battlecry(e.forEach(f.minionIsMurloc().and(f.anotherCard()), null, e.otherPT(0, 2))).card());
		game.addCard(minion( 3,      RARE, 1, 4, "Demolisher").on(HStoneTurnStartEvent.class, e.toRandom(f.enemy(), e.damage(2)), f.samePlayer()).card());
		game.addCard(minion( 3,      RARE, 2, 3, "Emperor Cobra").poison().card());
		game.addCard(minion( 3,      RARE, 1, 5, "Imp Master").on(HStoneTurnEndEvent.class, e.combined(damageSelf(1), e.summon("Imp")), f.samePlayer()).card());
		game.addCard(minion( 3,      RARE, 4, 7, "Injured Blademaster").battlecry(damageSelf(4)).card());
//		game.addCard(minion( 3,      RARE, 3, 3, "Mind Control Tech").effect("<b>Battlecry:</b>").effect("If your opponent has 4 or more minions, take control of one at random").card());
		game.addCard(minion( 3,      RARE, 2, 2, "Questing Adventurer").on(HStoneCardPlayedEvent.class, e.selfPT(1, 1), f.samePlayer().and(f.anotherCard())).card());
		game.addCard(minion( 3,      EPIC, 4, 2, "Big Game Hunter").battlecry(e.to(f.allMinions().and(f.withAttackMore(7)), e.destroyTarget())).card());
		game.addCard(minion( 3,		 EPIC, 3, 3, "Blood Knight").battlecry(e.forEach(f.minionWithDivineShield(), e.selfPT(3, 3), e.removeShield())).card());
//		game.addCard(minion( 3,      EPIC, 3, 3, "Murloc Warleader").effect("ALL other Murlocs have +2/+1").card());
//		game.addCard(minion( 3,      EPIC, 3, 3, "Southsea Captain").effect("Your other Pirates have +1/+1").card());
		game.addCard(minion( 3, LEGENDARY, 5, 5, "King Mukla").battlecry(giveOpponentCard("Bananas", 2)).card());
//		game.addCard(minion( 3, LEGENDARY, 3, 3, "Tinkmaster Overspark").battlecry("Transform another random minion into a 5/5 Devilsaur or a 1/1 Squirrel").card());
	}

	private HStoneEffect giveOpponentCard(String cardName, int count) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				CardZone<HStoneCard> zone = source.getPlayer().getNextPlayer().getHand();
				for (int i = 0; i < count; i++) {
					if (zone.size() < HStonePlayer.MAX_CARDS_IN_HAND)
						zone.createCardOnBottom(source.getGame().getCardModel(cardName));
				}
			}
		};
	}

	private HStoneEffect damageSelf(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				e.damage(damage).performEffect(source, source);
			}
		};
	}

}
