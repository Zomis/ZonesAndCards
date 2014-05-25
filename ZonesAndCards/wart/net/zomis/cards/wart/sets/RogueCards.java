package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.Battlecry.*;
import static net.zomis.cards.wart.factory.HSAbility.*;
import static net.zomis.cards.wart.factory.HSFilters.*;
import static net.zomis.cards.wart.factory.HSGetCounts.*;
import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSAction;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneEffect;


public class RogueCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 1, "Defias Bandit").card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Defias Ringleader").battlecry(comboOrNothing(summon("Defias Bandit"))).card());
		game.addCard(minion( 4,      RARE, 4, 4, "Master of Disguise").battlecry(to(samePlayer().and(allMinions()), giveAbility(HSAbility.STEALTH))).card());
		game.addCard(minion( 3,      RARE, 3, 3, "SI:7 Agent").battlecry(comboOrNothing(damage(2))).card()); // TODO: Only activate target selection mode if it's combo time
		game.addCard(minion( 6,      EPIC, 5, 3, "Kidnapper").battlecry(comboOrNothing(unsummon())).card());
		game.addCard(minion( 2,      EPIC, 1, 1, "Patient Assassin").stealth().poison().card());
		game.addCard(minion( 3, LEGENDARY, 2, 2, "Edwin VanCleef").battlecry(selfPT(edwinBonus, edwinBonus)).card());
		game.addCard(spell( 5,      FREE, "Assassinate").effect(to(opponentMinions(), destroyTarget())).card());
		game.addCard(spell( 0,      FREE, "Backstab").effect(to(allMinions().and(undamaged), damage(2))).card());
		game.addCard(spell( 1,      FREE, "Deadly Poison").effect(iff(haveWeapon, weaponBonus(2, 0))).card());
		game.addCard(spell( 2,      FREE, "Sap").effect(to(opponentMinions(), unsummon())).card());
		game.addCard(spell( 1,      FREE, "Sinister Strike").effect(damageToEnemyHero(3)).card());
		game.addCard(spell( 2,    COMMON, "Betrayal").effect(to(allMinions(), fromTargetToAdjacents(damage((src, dst) -> src.getAttack(), opponentMinions())))).card());
		game.addCard(spell( 1,    COMMON, "Cold Blood").effect(toMinion(ifElse(isCombo, otherPT(4, 0), otherPT(2, 0)))).card());
		game.addCard(spell( 1,    COMMON, "Conceal").effect(untilMyNextTurn(not(minionHasAbility(STEALTH)).and(samePlayer()).and(allMinions()), giveAbility(STEALTH), remove(STEALTH))).card());
		game.addCard(spell( 2,    COMMON, "Eviscerate").effect(toAny(ifElse(isCombo, damage(4), damage(2)))).card());
		game.addCard(spell( 3,    COMMON, "Fan of Knives").effect(combined(damageEnemyMinions(1), drawCard())).card());
//		game.addCard(spell( 0,    COMMON, "Shadowstep").effect("Return a friendly minion to your hand. It costs (2) less").card());
		game.addCard(spell( 2,    COMMON, "Shiv").effect(toAny(combined(damage(1), drawCard()))).card());
		game.addCard(spell( 7,    COMMON, "Sprint").effect(drawCards(4)).card());
		game.addCard(spell( 6,    COMMON, "Vanish").effect(forEach(allMinions(), null, unsummon())).card());
		game.addCard(spell( 2,      RARE, "Blade Flurry").effect(iff(haveWeapon, combined(forEach(all().and(opponent()), null, damage(calcWeaponDamage, all())), destroyMyWeapon()))).card());
//		game.addCard(spell( 3,      RARE, "Headcrack").effect("Deal 2 damage to the enemy hero").effect("<b>Combo:</b>").effect("Return this to your hand next turn").card());
//		game.addCard(spell( 0,      EPIC, "Preparation").effect("The next spell you cast this turn costs (3) less").card());
		game.addCard(weapon( 1,      FREE, 1, 2, "Wicked Knife").card());
		game.addCard(weapon( 5,    COMMON, 3, 4, "Assassin's Blade").card());
		game.addCard(weapon( 3,      RARE, 2, 2, "Perdition's Blade").battlecry(ifElse(isCombo, damage(2), damage(1))).card());
	}

	private HSAction fromTargetToAdjacents(HStoneEffect effect) {
		return new HSAction() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				adjacents(effect).performEffect(target, target);
			}
		};
	}


}
