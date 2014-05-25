package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;

import java.util.List;

import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneDamageDealtEvent;
import net.zomis.cards.wart.events.HStoneDamagedEvent;
import net.zomis.cards.wart.events.HStoneMinionSummonedEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.utils.ZomisList;

public class WarriorCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		HSFilters f = new HSFilters();
		game.addCard(minion( 3,      FREE, 2, 3, "Warsong Commander").on(HStoneMinionSummonedEvent.class, e.giveAbility(HSAbility.CHARGE), f.samePlayer().and(f.withAttackLess(3))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 4,    COMMON, 3, 3, "Arathi Weaponsmith").battlecry(e.equip("Battle Axe")).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Cruel Taskmaster").battlecry(e.toMinion(e.combined(e.damage(1), e.otherPT(2, 0)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 4,    COMMON, 4, 3, "Kor'kron Elite").charge().forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 2,      RARE, 1, 4, "Armorsmith").on(HStoneDamagedEvent.class, e.armor(1), f.samePlayer().and(f.allMinions())).forClass(HStoneClass.WARRIOR).card()); //  effect("Whenever a friendly minion takes damage, gain 1 Armor")
		game.addCard(minion( 3,      RARE, 2, 4, "Frothing Berserker").on(HStoneDamagedEvent.class, e.selfPT(1, 0), f.allMinions()).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 8, LEGENDARY, 4, 9, "Grommash Hellscream").charge().staticPT(f.enrage(), 6, 0).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 3,      FREE, "Charge").effect(e.to(f.samePlayer().and(f.allMinions()), e.combined(e.otherPT(2, 0), e.giveAbility(HSAbility.CHARGE)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      FREE, "Execute").effect(e.to(f.opponent().and(f.isDamaged()).and(f.allMinions()), e.destroyTarget())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,      FREE, "Heroic Strike").effect(e.tempBoostToMyHero(4, 0)).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Battle Rage").effect(e.forEach(f.samePlayer().and(f.isDamaged()), e.drawCard(), null)).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Cleave").effect(e.iff(f.countAtLeast(f.opponentMinions(), 2), e.toMultipleRandom(c.fixed(2), f.opponentMinions(), e.damage(2)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 0,    COMMON, "Inner Rage").effect(e.toMinion(e.combined(e.damage(1), e.otherPT(2, 0)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Rampage").effect(e.to(f.isDamaged().and(f.allMinions()), e.otherPT(3, 3))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 3,    COMMON, "Shield Block").effect(e.combined(e.armor(5), e.drawCard())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Slam").effect(e.toMinion(performAndPerformIf(e.damage(2), e.drawCard(), f.isTargetAlive))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,    COMMON, "Whirlwind").effect(e.forEach(f.allMinions(), null, e.damage(1))).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 2,      RARE, "Commanding Shout").effect("Your minions can't be reduced below 1 Health this turn.  Draw a card").forClass(HStoneClass.WARRIOR).card()); // TODO: Use enchantment for "Commanding Shout"
		game.addCard(spell( 4,      RARE, "Mortal Strike").effect(e.toAny(deal4or6damageIfIHaveLessThan12Health)).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      RARE, "Upgrade!").effect(e.ifElse(f.haveWeapon, e.weaponBonus(1, 1), e.equip("Heavy Axe"))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 5,      EPIC, "Brawl").effect(destroyAllMinionsExceptOne()).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      EPIC, "Shield Slam").effect(e.damage(c.calcHeroArmor, f.allMinions())).forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 1,      NONE, 2, 2, "Battle Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 1,      NONE, 1, 3, "Heavy Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 2,      FREE, 3, 2, "Fiery War Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 5,    COMMON, 5, 2, "Arcanite Reaper").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 7,      EPIC, 7, 1, "Gorehowl").on(HStoneDamageDealtEvent.class, cost1attackInsteadOf1durability(), f.thisCard()).forClass(HStoneClass.WARRIOR).card());
	}

	public static HStoneEffect destroyAllMinionsExceptOne() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> minions = source.getGame().findAll(source, f.allMinions());
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

	public static final HStoneEffect deal4or6damageIfIHaveLessThan12Health = 
			e.ifElse((src, dst) -> src.getPlayer().getHealth() <= 12, e.damage(6), e.damage(4));

}
