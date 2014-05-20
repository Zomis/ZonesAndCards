package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneHealEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.hstone.factory.HSTargetType;
import net.zomis.cards.util.CardSet;

public class PriestCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      FREE, 1, 3, "Northshire Cleric").on(HStoneHealEvent.class, drawCard(), HSTargetType.MINION).card());
		game.addCard(minion( 4,    COMMON, 0, 5, "Lightspawn").battlecry(enchantSelfWithAttackEqualsHealth()).card());
		game.addCard(minion( 6,    COMMON, 6, 6, "Temple Enforcer").battlecry(otherPT(0, 3)).card());
//		game.addCard(minion( 4,      RARE, 3, 5, "Auchenai Soulpriest").effect("Your cards and powers that restore Health now deal damage instead").card());
		game.addCard(minion( 2,      RARE, 0, 5, "Lightwell").on(HStoneTurnStartEvent.class, null, samePlayer()).card()); // "At the start of your turn, restore 3 Health to a damaged friendly character"
		game.addCard(minion( 6,      EPIC, 4, 5, "Cabal Shadow Priest").battlecry(stealMinion(withAttackLess(2))).card());
		game.addCard(minion( 0,      EPIC, 0, 1, "Shadow of Nothing").funText("Mindgames whiffed! Your opponent had no minions!").card());
//		game.addCard(minion( 7, LEGENDARY, 7, 7, "Prophet Velen").effect("Double the damage and healing of your spells and Hero Power").card());
		game.addCard(spell( 1,      FREE, "Holy Smite").effect(damage(2)).card());
		game.addCard(spell( 2,      FREE, "Mind Blast").effect(damageToOppHero(5)).card());
		game.addCard(spell( 1,      FREE, "Power Word: Shield").effect(toMinion(otherPT(0, 2))).card());
		game.addCard(spell( 2,      FREE, "Shadow Word: Pain").effect(destroyMinion(withAttackLess(3))).card());
		game.addCard(spell( 0,    COMMON, "Circle of Healing").effect(healAll(4, all())).card());
		game.addCard(spell( 2,    COMMON, "Divine Spirit").effect(doubleHealth()).card());
		game.addCard(spell( 5,    COMMON, "Holy Nova").effect(combined(forEach(not(samePlayer()), null, damage(2)), forEach(samePlayer(), heal(2), null))).card());
		game.addCard(spell( 1,    COMMON, "Inner Fire").effect(setOtherAttackEqualsHealth()).card());
		game.addCard(spell(10,    COMMON, "Mind Control").effect(stealMinion(all())).card());
		game.addCard(spell( 1,    COMMON, "Mind Vision").effect(copyOppCardInHand()).card());
		game.addCard(spell( 3,    COMMON, "Shadow Word: Death").effect(destroyMinion(withAttackMore(5))).card());
		game.addCard(spell( 0,    COMMON, "Silence").effect(silencer()).card());
		game.addCard(spell( 3,    COMMON, "Thoughtsteal").effect(copyTwoCardsInOppDeckToMyHand()).card());
		game.addCard(spell( 6,      RARE, "Holy Fire").effect(combined(damage(5), healMyHero(5))).card());
		game.addCard(spell( 4,      RARE, "Mass Dispel").effect(combined(drawCard(), forEach(opponentMinions(), null, silencer()))).card());
		game.addCard(spell( 4,      RARE, "Shadow Madness").effect(stealMinionUntilEndOfTurn(withAttackLess(3))).card());
		game.addCard(spell( 4,      EPIC, "Mindgames").effect(copyMinionInOppDeckToMyBattlefield()).card());
		game.addCard(spell( 3,      EPIC, "Shadowform").effect(shadowform()).card());
	}


}
