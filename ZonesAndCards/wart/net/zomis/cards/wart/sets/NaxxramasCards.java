package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneMinionDiesEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSFilters;

public class NaxxramasCards implements CardSet<HStoneGame> {

	private static final Battlecry e = new Battlecry();
	private static final HSFilters f = new HSFilters();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 3,      RARE, 4, 4, "Nerubian").card());
		game.addCard(minion( 1,    COMMON, 1, 2, "Slime").taunt().card());
		game.addCard(minion( 1,    COMMON, 1, 1, "Spectral Spider").card());
		game.addCard(minion(10, LEGENDARY, 11, 11, "Nerubian").card());
		
		game.addCard(minion( 3,    COMMON, 4, 4, "Dancing Swords").deathrattle(e.oppDraw(1)).card());
		game.addCard(minion( 2,    COMMON, 1, 2, "Haunted Creeper").deathrattle(e.summon("Spectral Spider", 2)).card());
//		game.addCard(minion( 2,    COMMON, 2, 2, "Mad Scientist").deathrattle(/* secret from deck to battlefield */).card());
//		game.addCard(minion( 2,    COMMON, 1, 4, "Nerub'ar Weblord").staticMana(/* minions with battlecry */, 2 more).card());
		game.addCard(minion( 5,    COMMON, 4, 6, "Spectral Knight").shroud().card());
//		game.addCard(minion( 3,    COMMON, 1, 4, "Stoneskin Gargoyle").on(HStoneTurnStartEvent.class, e.healFull(), f.thisCard()).card());
//		game.addCard(minion( 1,    COMMON, 1, 2, "Undertaker").on(HStoneMinionSummonedEvent.class, e.selfPT(1, 1), f.hasDeathrattle).card());
		game.addCard(minion( 2,    COMMON, 1, 3, "Unstable Ghoul").taunt().deathrattle(e.forEach(f.allMinions(), null, e.damage(1))).card());
		game.addCard(minion( 1,    COMMON, 2, 3, "Zombie Chow").deathrattle(e.healAll(5, f.allPlayers().and(f.opponent()))).card());
//		game.addCard(minion( 3,      RARE, 2, 8, "Deathlord").taunt().deathrattle(/* opponent minion from deck to battlefield */).card());
		game.addCard(minion( 2,      RARE, 0, 2, "Nerubian Egg").deathrattle(e.summon("Nerubian")).card());
		game.addCard(minion( 5,      RARE, 3, 5, "Sludge Belcher").taunt().deathrattle(e.summon("Slime")).card());
		game.addCard(minion( 4,      RARE, 3, 5, "Wailing Soul").battlecry(e.forEach(f.samePlayer().and(f.allMinions()), null, e.silencer())).card());
//		game.addCard(minion( 2,      EPIC, 1, 2, "Echoing Ooze").on(HStoneTurnEndEvent.class, /* summon copy of this card */, /* this turn */).card());
		game.addCard(minion( 3,      RARE, 2, 2, "Shade of Naxxramas").stealth().on(HStoneTurnStartEvent.class, e.selfPT(1, 1), f.samePlayer()).card());
//		game.addCard(minion( 4, LEGENDARY, 1, 7, "Baron Rivendare").effect(/* your minions trigger deathrattle twice */).card());
//		game.addCard(minion( 5, LEGENDARY, 4, 7, "Feugen").effect("if stalagg also died this game, summon thaddius").card());
//		game.addCard(minion( 8, LEGENDARY, 6, 8, "Kel'Thuzad").on(HStoneTurnEndEvent.class, "summon all friendly minions that died this turn", f.all()).card());
//		game.addCard(minion( 5, LEGENDARY, 5, 5, "Loatheb").battlecry("enemy spells costs 5 more next turn").card());
		game.addCard(minion( 6, LEGENDARY, 2, 8, "Maexxna").poison().card());
//		game.addCard(minion( 5, LEGENDARY, 7, 4, "Stalagg").deathrattle("If Feugen also died this game, summon Thaddius").card());
		
//		game.addCard(minion( 1, COMMON, 1, 1, "Webspinner").deathrattle(/* add a random beast card to your hand */).forClass(HStoneClass.HUNTER).card());
		game.addCard(minion( 6, COMMON, 2, 8, "Dark Cultist").deathrattle(e.toRandom(f.samePlayer().and(f.allMinions()), e.otherPT(0, 3))).forClass(HStoneClass.PRIEST).card());
		game.addCard(minion( 6, COMMON, 2, 8, "Anub'ar Ambusher").deathrattle(e.toRandom(f.friendlyMinion, e.unsummon())).forClass(HStoneClass.ROGUE).card());
//		game.addCard(minion( 6, COMMON, 2, 8, "Voidcaller").deathrattle(/* put a random demon from your hand into the battlefield */).forClass(HStoneClass.WARLOCK).card());
		
//		game.addCard(spell(4, COMMON, "Poison Seeds").effect(e.forEach(f.allMinions(), null, e.combined(e.destroyTarget(), e.summonTargetPlayer("Treant")))).forClass(HStoneClass.DRUID).card());
//		game.addCard(spell(4, COMMON, "Duplicate").secret(HStoneMinionDiesEvent.class, e.copyTargetCardToHand(), f.friendlyMinion).forClass(HStoneClass.MAGE).card());
		game.addCard(spell(4, COMMON, "Avenge").secret(HStoneMinionDiesEvent.class, e.toRandom(f.friendlyMinion, e.otherPT(3, 2)), f.friendlyMinion).forClass(HStoneClass.PALADIN).card());
//		game.addCard(spell(4, COMMON, "Reincarnate").effect(/* Destroy minion, then return it to life with full health */).forClass(HStoneClass.DRUID).card());
		game.addCard(weapon(4, COMMON, 4, 2, "Death's Bite").deathrattle(e.forEach(f.allMinions(), null, e.damage(1))).forClass(HStoneClass.WARRIOR).card());
		
		
		
	}

}
