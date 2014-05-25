package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HSAbility.*;
import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSAction;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneEffect;


public class RogueCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 1, "Defias Bandit").card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Defias Ringleader").battlecry(e.comboOrNothing(e.summon("Defias Bandit"))).card());
		game.addCard(minion( 4,      RARE, 4, 4, "Master of Disguise").battlecry(e.to(f.samePlayer().and(f.allMinions()), e.giveAbility(HSAbility.STEALTH))).card());
		game.addCard(minion( 3,      RARE, 3, 3, "SI:7 Agent").battlecry(e.comboOrNothing(e.damage(2))).card()); // TODO: Only activate target selection mode if it's combo time
		game.addCard(minion( 6,      EPIC, 5, 3, "Kidnapper").battlecry(e.comboOrNothing(e.unsummon())).card());
		game.addCard(minion( 2,      EPIC, 1, 1, "Patient Assassin").stealth().poison().card());
		game.addCard(minion( 3, LEGENDARY, 2, 2, "Edwin VanCleef").battlecry(e.selfPT(c.edwinBonus, c.edwinBonus)).card());
		game.addCard(spell( 5,      FREE, "Assassinate").effect(e.to(f.opponentMinions(), e.destroyTarget())).card());
		game.addCard(spell( 0,      FREE, "Backstab").effect(e.to(f.allMinions().and(f.undamaged), e.damage(2))).card());
		game.addCard(spell( 1,      FREE, "Deadly Poison").effect(e.iff(f.haveWeapon, e.weaponBonus(2, 0))).card());
		game.addCard(spell( 2,      FREE, "Sap").effect(e.to(f.opponentMinions(), e.unsummon())).card());
		game.addCard(spell( 1,      FREE, "Sinister Strike").effect(e.damageToEnemyHero(3)).card());
		game.addCard(spell( 2,    COMMON, "Betrayal").effect(e.to(f.allMinions(), fromTargetToAdjacents(e.damage((src, dst) -> src.getAttack(), f.opponentMinions())))).card());
		game.addCard(spell( 1,    COMMON, "Cold Blood").effect(e.toMinion(e.ifElse(f.isCombo, e.otherPT(4, 0), e.otherPT(2, 0)))).card());
		game.addCard(spell( 1,    COMMON, "Conceal").effect(e.untilMyNextTurn(f.not(f.minionHasAbility(STEALTH)).and(f.samePlayer()).and(f.allMinions()), e.giveAbility(STEALTH), e.remove(STEALTH))).card());
		game.addCard(spell( 2,    COMMON, "Eviscerate").effect(e.toAny(e.ifElse(f.isCombo, e.damage(4), e.damage(2)))).card());
		game.addCard(spell( 3,    COMMON, "Fan of Knives").effect(e.combined(e.damageEnemyMinions(1), e.drawCard())).card());
//		game.addCard(spell( 0,    COMMON, "Shadowstep").effect("Return a friendly minion to your hand. It costs (2) less").card());
		game.addCard(spell( 2,    COMMON, "Shiv").effect(e.toAny(e.combined(e.damage(1), e.drawCard()))).card());
		game.addCard(spell( 7,    COMMON, "Sprint").effect(e.drawCards(4)).card());
		game.addCard(spell( 6,    COMMON, "Vanish").effect(e.forEach(f.allMinions(), null, e.unsummon())).card());
		game.addCard(spell( 2,      RARE, "Blade Flurry").effect(e.iff(f.haveWeapon, e.combined(e.forEach(f.all().and(f.opponent()), null, e.damage(c.calcWeaponDamage, f.all())), e.destroyMyWeapon()))).card());
//		game.addCard(spell( 3,      RARE, "Headcrack").effect("Deal 2 damage to the enemy hero").effect("<b>Combo:</b>").effect("Return this to your hand next turn").card());
//		game.addCard(spell( 0,      EPIC, "Preparation").effect("The next spell you cast this turn costs (3) less").card());
		game.addCard(weapon( 1,      FREE, 1, 2, "Wicked Knife").card());
		game.addCard(weapon( 5,    COMMON, 3, 4, "Assassin's Blade").card());
		game.addCard(weapon( 3,      RARE, 2, 2, "Perdition's Blade").battlecry(e.ifElse(f.isCombo, e.damage(2), e.damage(1))).card());
	}

	private HSAction fromTargetToAdjacents(HStoneEffect effect) {
		return new HSAction() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				e.adjacents(effect).performEffect(target, target);
			}
		};
	}


}
