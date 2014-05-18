package net.zomis.cards.hstone.sets;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneDamageDealtEvent;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;
import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;

public class WarriorCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
//		game.addCard(minion( 3,      FREE, 2, 3, "Warsong Commander").effect("Whenever you summon a minion with 3 or less Attack, give it").effect("<b>Charge</b>").effect("").forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 4,    COMMON, 3, 3, "Arathi Weaponsmith").battlecry(equip("Battle Axe")).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Cruel Taskmaster").battlecry(toMinion(combined(damage(1), otherPT(2, 0)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(minion( 4,    COMMON, 4, 3, "Kor'kron Elite").charge().forClass(HStoneClass.WARRIOR).card());
//		game.addCard(minion( 2,      RARE, 1, 4, "Armorsmith").effect("Whenever a friendly minion takes damage, gain 1 Armor").forClass(HStoneClass.WARRIOR).card());
//		game.addCard(minion( 3,      RARE, 2, 4, "Frothing Berserker").effect("Whenever a minion takes damage, gain +1 Attack").forClass(HStoneClass.WARRIOR).card());
//		game.addCard(minion( 8, LEGENDARY, 4, 9, "Grommash Hellscream").charge().effect("<b>Enrage:</b>").effect("+6 Attack").forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 3,      FREE, "Charge").effect(to(and(samePlayer(), allMinions()), combined(otherPT(2, 0), giveAbility(HSAbility.CHARGE)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,      FREE, "Execute").effect(to(and(not(undamaged()), not(samePlayer()), allMinions()), destroyTarget())).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 2,      FREE, "Heroic Strike").effect("Give your hero +4 Attack this turn").forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Battle Rage").effect(forEach(and(samePlayer(), not(undamaged())), drawCard(), null)).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 2,    COMMON, "Cleave").effect("Deal 2 damage to two random enemy minions").forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 0,    COMMON, "Inner Rage").effect(toMinion(combined(damage(1), otherPT(2, 0)))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 2,    COMMON, "Rampage").effect(to(and(not(undamaged()), allMinions()), otherPT(3, 3))).forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 3,    COMMON, "Shield Block").effect(combined(armor(5), drawCard())).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 2,    COMMON, "Slam").effect("Deal 2 damage to a minion.  If it survives, draw a card").forClass(HStoneClass.WARRIOR).card());
		game.addCard(spell( 1,    COMMON, "Whirlwind").effect(forEach(allMinions(), null, damage(1))).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 2,      RARE, "Commanding Shout").effect("Your minions can't be reduced below 1 Health this turn.  Draw a card").forClass(HStoneClass.WARRIOR).card()); // TODO: Use enchantment for "Commanding Shout"
		game.addCard(spell( 4,      RARE, "Mortal Strike").effect(toAny(deal4or6damageIfIHaveLessThan12Health())).forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 1,      RARE, "Upgrade!").effect("If you have a weapon, give it +1/+1.  Otherwise equip a 1/3 weapon").forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 5,      EPIC, "Brawl").effect("Destroy all minions except one.  (chosen randomly)").forClass(HStoneClass.WARRIOR).card());
//		game.addCard(spell( 1,      EPIC, "Shield Slam").effect("Deal 1 damage to a minion for each Armor you have").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 1,      NONE, 2, 2, "Battle Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 1,      NONE, 1, 3, "Heavy Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 2,      FREE, 3, 2, "Fiery War Axe").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 5,    COMMON, 5, 2, "Arcanite Reaper").forClass(HStoneClass.WARRIOR).card());
		game.addCard(weapon( 7,      EPIC, 7, 1, "Gorehowl").on(HStoneDamageDealtEvent.class, cost1attackInsteadOf1durability(), thisCard()).forClass(HStoneClass.WARRIOR).card());
	}

	private HStoneEffect cost1attackInsteadOf1durability() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO Attacking a minion costs 1 Attack instead of 1 Durability
			}
		};
	}

	private HStoneEffect deal4or6damageIfIHaveLessThan12Health() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
//			"Deal 4 damage.  If you have 12 or less Health, deal 6 instead"	
				int damage = source.getPlayer().getHealth() <= 12 ? 6 : 4;
				damage(damage).performEffect(source, target);
			}
		};
	}

}
