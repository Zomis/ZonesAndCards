package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneHealEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSFilters;

public class PriestCards implements CardSet<HStoneGame> {

	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      FREE, 1, 3, "Northshire Cleric").on(HStoneHealEvent.class, e.drawCard(), f.allMinions()).card());
		game.addCard(minion( 4,    COMMON, 0, 5, "Lightspawn").effect(e.enchantSelfWithAttackEqualsHealth()).card());
		game.addCard(minion( 6,    COMMON, 6, 6, "Temple Enforcer").battlecry(e.to(f.allMinions().and(f.samePlayer()), e.otherPT(0, 3))).card());
//		game.addCard(minion( 4,      RARE, 3, 5, "Auchenai Soulpriest").effect("Your cards and powers that restore Health now deal damage instead").card());
		game.addCard(minion( 2,      RARE, 0, 5, "Lightwell").on(HStoneTurnStartEvent.class, null, f.samePlayer()).card()); // "At the start of your turn, restore 3 Health to a damaged friendly character"
		game.addCard(minion( 6,      EPIC, 4, 5, "Cabal Shadow Priest").battlecry(e.stealMinion(f.withAttackLess(2))).card());
		game.addCard(minion( 0,      EPIC, 0, 1, "Shadow of Nothing").funText("Mindgames whiffed! Your opponent had no minions!").card());
//		game.addCard(minion( 7, LEGENDARY, 7, 7, "Prophet Velen").effect("Double the damage and healing of your spells and Hero Power").card());
		game.addCard(spell( 1,      FREE, "Holy Smite").effect(e.damage(2)).card());
		game.addCard(spell( 2,      FREE, "Mind Blast").effect(e.damageToOppHero(5)).card());
		game.addCard(spell( 1,      FREE, "Power Word: Shield").effect(e.toMinion(e.otherPT(0, 2))).card());
		game.addCard(spell( 2,      FREE, "Shadow Word: Pain").effect(e.destroyMinion(f.withAttackLess(3))).card());
		game.addCard(spell( 0,    COMMON, "Circle of Healing").effect(e.healAll(4, f.all())).card());
		game.addCard(spell( 2,    COMMON, "Divine Spirit").effect(e.doubleHealth()).card());
		game.addCard(spell( 5,    COMMON, "Holy Nova").effect(e.combined(e.forEach(f.opponent(), null, e.damage(2)), e.forEach(f.samePlayer(), e.heal(2), null))).card());
		game.addCard(spell( 1,    COMMON, "Inner Fire").effect(e.to(f.allMinions(), e.setOtherAttackEqualsHealth())).card());
		game.addCard(spell(10,    COMMON, "Mind Control").effect(e.stealMinion(f.all())).card());
		game.addCard(spell( 1,    COMMON, "Mind Vision").effect(e.copyOppCardInHand()).card());
		game.addCard(spell( 3,    COMMON, "Shadow Word: Death").effect(e.destroyMinion(f.withAttackMore(5))).card());
		game.addCard(spell( 0,    COMMON, "Silence").effect(e.silencer()).card());
		game.addCard(spell( 3,    COMMON, "Thoughtsteal").effect(e.copyTwoCardsInOppDeckToMyHand()).card());
		game.addCard(spell( 6,      RARE, "Holy Fire").effect(e.to(f.all(), e.combined(e.damage(5), e.healMyHero(5)))).card());
		game.addCard(spell( 4,      RARE, "Mass Dispel").effect(e.combined(e.drawCard(), e.forEach(f.opponentMinions(), null, e.silencer()))).card());
		game.addCard(spell( 4,      RARE, "Shadow Madness").effect(e.stealMinionUntilEndOfTurn(f.withAttackLess(3))).card());
		game.addCard(spell( 4,      EPIC, "Mindgames").effect(e.copyMinionInOppDeckToMyBattlefield()).card());
		game.addCard(spell( 3,      EPIC, "Shadowform").effect(e.shadowform()).card());
	}


}
