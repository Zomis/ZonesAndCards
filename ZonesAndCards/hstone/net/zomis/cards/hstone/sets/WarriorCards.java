package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HSGetCounts.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;

import java.util.List;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneDamageDealtEvent;
import net.zomis.cards.hstone.events.HStoneDamagedEvent;
import net.zomis.cards.hstone.events.HStoneMinionSummonedEvent;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;
import net.zomis.utils.ZomisList;

public class WarriorCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 3,      FREE, 2, 3, "Warsong Commander").on(HStoneMinionSummonedEvent.class, giveAbility(HSAbility.CHARGE), samePlayer().and(withAttackLess(3))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 4,    COMMON, 3, 3, "Arathi Weaponsmith").battlecry(equip("Battle Axe")).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Cruel Taskmaster").battlecry(toMinion(combined(damage(1), otherPT(2, 0)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 4,    COMMON, 4, 3, "Kor'kron Elite").charge().forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 2,      RARE, 1, 4, "Armorsmith").on(HStoneDamagedEvent.class, armor(1), samePlayer().and(allMinions())).forClass(HStoneClass.WARRIOR).card()); //  effect("Whenever a friendly minion takes damage, gain 1 Armor")
		game.addCard(minion( 3,      RARE, 2, 4, "Frothing Berserker").on(HStoneDamagedEvent.class, selfPT(1, 0), allMinions()).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 8, LEGENDARY, 4, 9, "Grommash Hellscream").charge().staticPT(enrage(), 6, 0).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 3,      FREE, "Charge").effect(to(samePlayer().and(allMinions()), combined(otherPT(2, 0), giveAbility(HSAbility.CHARGE)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      FREE, "Execute").effect(to(opponentPlayer().and(isDamaged()).and(allMinions()), destroyTarget())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,      FREE, "Heroic Strike").effect(tempBoostToMyHero(4, 0)).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Battle Rage").effect(forEach(samePlayer().and(isDamaged()), drawCard(), null)).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Cleave").effect(iff(countAtLeast(opponentMinions(), 2), toMultipleRandom(fixed(2), opponentMinions(), damage(2)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 0,    COMMON, "Inner Rage").effect(toMinion(combined(damage(1), otherPT(2, 0)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Rampage").effect(to(isDamaged().and(allMinions()), otherPT(3, 3))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 3,    COMMON, "Shield Block").effect(combined(armor(5), drawCard())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Slam").effect(toMinion(performAndPerformIf(damage(2), drawCard(), isTargetAlive()))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,    COMMON, "Whirlwind").effect(forEach(allMinions(), null, damage(1))).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 2,      RARE, "Commanding Shout").effect("Your minions can't be reduced below 1 Health this turn.  Draw a card").forClass(HStoneClass.WARRIOR).card()); // TODO: Use enchantment for "Commanding Shout"
		game.addCard(spell( 4,      RARE, "Mortal Strike").effect(toAny(deal4or6damageIfIHaveLessThan12Health())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      RARE, "Upgrade!").effect(ifElse(haveWeapon(), weaponBonus(1, 1), equip("Heavy Axe"))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 5,      EPIC, "Brawl").effect(destroyAllMinionsExceptOne()).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      EPIC, "Shield Slam").effect(damage(calcHeroArmor(), allMinions())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 1,      NONE, 2, 2, "Battle Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 1,      NONE, 1, 3, "Heavy Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 2,      FREE, 3, 2, "Fiery War Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 5,    COMMON, 5, 2, "Arcanite Reaper").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 7,      EPIC, 7, 1, "Gorehowl").on(HStoneDamageDealtEvent.class, cost1attackInsteadOf1durability(), thisCard()).forClass(HStoneClass.WARRIOR).card());
	}

	public static HSFilter countAtLeast(HSFilter filter, int moreThanOrEqualTo) {
		return (src, dst) -> src.getGame().findAll(src, filter).size() >= moreThanOrEqualTo;
	}

	public static HStoneEffect destroyAllMinionsExceptOne() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> minions = source.getGame().findAll(source, allMinions());
				HStoneCard survivor = ZomisList.getRandom(minions, source.getGame().getRandom());
				minions.remove(survivor);
				for (HStoneCard card : minions) {
					card.destroy();
				}
			}
		};
	}

	public static HStoneEffect performAndPerformIf(HStoneEffect firstEffect, HStoneEffect conditionalEffect, HSFilter condition) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				firstEffect.performEffect(source, target);
				if (condition.shouldKeep(source, target))
					conditionalEffect.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect cost1attackInsteadOf1durability() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO: Attacking a minion costs 1 Attack instead of 1 Durability
			}
		};
	}

	public static HStoneEffect deal4or6damageIfIHaveLessThan12Health() {
		return ifElse((src, dst) -> src.getPlayer().getHealth() <= 12, damage(6), damage(4));
	}

}
