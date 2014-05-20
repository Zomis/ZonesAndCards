package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSAbility.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.util.CardSet;


public class RogueCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 1, "Defias Bandit").card());
//		game.addCard(minion( 2,    COMMON, 2, 2, "Defias Ringleader").effect("<b>Combo:</b>").effect("Summon a 2/1 Defias Bandit").card());
		game.addCard(minion( 4,      RARE, 4, 4, "Master of Disguise").battlecry(to(samePlayer().and(allMinions()), giveAbility(HSAbility.STEALTH))).card());
//		game.addCard(minion( 3,      RARE, 3, 3, "SI:7 Agent").effect("<b>Combo:</b>").effect("Deal 2 damage").card());
//		game.addCard(minion( 6,      EPIC, 5, 3, "Kidnapper").effect("<b>Combo:</b>").effect("Return a minion to its owner's hand").card());
//		game.addCard(minion( 2,      EPIC, 1, 1, "Patient Assassin").effect("<b>Stealth</b>").effect("Destroy any minion damaged by this minion").card());
//		game.addCard(minion( 3, LEGENDARY, 2, 2, "Edwin VanCleef").effect("<b>Combo:</b>").effect("Gain +2/+2 for each card played earlier this turn").card());
		game.addCard(spell( 5,      FREE, "Assassinate").effect(to(opponentMinions(), destroyTarget())).card());
		game.addCard(spell( 0,      FREE, "Backstab").effect(to(allMinions().and(undamaged()), damage(2))).card());
//		game.addCard(spell( 1,      FREE, "Deadly Poison").effect("Give your weapon +2 Attack").card());
//		game.addCard(spell( 2,      FREE, "Sap").effect("Return an enemy minion to your opponent's hand").card());
//		game.addCard(spell( 1,      FREE, "Sinister Strike").effect("Deal 3 damage to the enemy hero").card());
//		game.addCard(spell( 2,    COMMON, "Betrayal").effect("Force an enemy minion to deal its damage to the minions next to it").card());
//		game.addCard(spell( 1,    COMMON, "Cold Blood").effect("Give a minion +2 Attack").effect("<b>Combo:</b>").effect("+4 Attack instead").card());
		game.addCard(spell( 1,    COMMON, "Conceal").effect(untilMyNextTurn(not(minionHasAbility(STEALTH)).and(samePlayer()).and(allMinions()), giveAbility(STEALTH), remove(STEALTH))).card());
//		game.addCard(spell( 2,    COMMON, "Eviscerate").effect("Deal 2 damage").effect("<b>Combo:</b>").effect("Deal 4 damage instead").card());
		game.addCard(spell( 3,    COMMON, "Fan of Knives").effect(combined(damageEnemyMinions(1), drawCard())).card());
//		game.addCard(spell( 0,    COMMON, "Shadowstep").effect("Return a friendly minion to your hand. It costs (2) less").card());
		game.addCard(spell( 2,    COMMON, "Shiv").effect(toAny(combined(damage(1), drawCard()))).card());
		game.addCard(spell( 7,    COMMON, "Sprint").effect(drawCards(4)).card());
//		game.addCard(spell( 6,    COMMON, "Vanish").effect("Return all minions to their owner's hand").card());
//		game.addCard(spell( 2,      RARE, "Blade Flurry").effect("Destroy your weapon and deal its damage to all enemies").card());
//		game.addCard(spell( 3,      RARE, "Headcrack").effect("Deal 2 damage to the enemy hero").effect("<b>Combo:</b>").effect("Return this to your hand next turn").card());
//		game.addCard(spell( 0,      EPIC, "Preparation").effect("The next spell you cast this turn costs (3) less").card());
		game.addCard(weapon( 1,      FREE, 1, 2, "Wicked Knife").card());
		game.addCard(weapon( 5,    COMMON, 3, 4, "Assassin's Blade").card());
//		game.addCard(weapon( 3,      RARE, 2, 2, "Perdition's Blade").effect("<b>Battlecry:</b>").effect("Deal 1 damage").effect("<b>Combo:</b>").effect("Deal 2 instead").card());
	}


}
