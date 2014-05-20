package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.Battlecry.HSGetCount;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HSTargetType;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;


public class PaladinCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      FREE, 1, 1, "Silver Hand Recruit").card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Argent Protector").battlecry(giveAbility(HSAbility.DIVINE_SHIELD)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Defender").card());
		game.addCard(minion( 7,    COMMON, 5, 6, "Guardian of Kings").battlecry(healMyHero(6)).card());
		game.addCard(minion( 3,      RARE, 3, 3, "Aldor Peacekeeper").battlecry(setAttack(1)).card());
		game.addCard(minion( 8, LEGENDARY, 6, 6, "Tirion Fordring").shield().taunt().deathrattle(equip("Ashbringer")).card());
		game.addCard(spell( 1,      FREE, "Blessing of Might").effect(toMinion(otherPT(3, 0))).card());
		game.addCard(spell( 4,      FREE, "Hammer of Wrath").effect(combined(drawCard(), damage(3))).card());
		game.addCard(spell( 1,      FREE, "Hand of Protection").effect(toMinion(giveAbility(HSAbility.DIVINE_SHIELD))).card());
		game.addCard(spell( 2,      FREE, "Holy Light").effect(heal(6)).card());
		game.addCard(spell( 4,    COMMON, "Blessing of Kings").effect(toMinion(otherPT(4, 4))).card());
		game.addCard(spell( 1,    COMMON, "Blessing of Wisdom").effect(addEnchantOnAttackDrawCard()).card());
		game.addCard(spell( 4,    COMMON, "Consecration").effect(forEach(not(samePlayer()), null, damage(2))).card());
//		game.addCard(spell( 1,    COMMON, "Eye for an Eye").effect("<b>Secret:</b>").effect("When your hero takes damage, deal that much damage to the enemy hero").card());
		game.addCard(spell( 1,    COMMON, "Humility").effect(toMinion(setAttack(1))).card());
//		game.addCard(spell( 1,    COMMON, "Noble Sacrifice").effect("<b>Secret:</b>").effect("When an enemy attacks, summon a 2/1 Defender as the new target").card());
//		game.addCard(spell( 1,    COMMON, "Redemption").effect("<b>Secret:</b>").effect("When one of your minions dies, return it to life with 1 Health").card());
//		game.addCard(spell( 1,    COMMON, "Repentance").effect("<b>Secret:</b>").effect("When your opponent plays a minion, reduce its Health to 1").card());
//		game.addCard(spell( 5,      RARE, "Blessed Champion").effect( "Double a minion's Attack").card());
		game.addCard(spell( 3,      RARE, "Divine Favor").effect(repeat(oppHandSizeMinusMyHandSize(), drawCard())).card());
		game.addCard(spell( 2,      RARE, "Equality").effect(forEach(allMinions(), null, set(HStoneRes.HEALTH, 1))).card());
		game.addCard(spell( 5,      RARE, "Holy Wrath").effect(toAny(drawCardAndDealDamageEqualToCost())).card());
		game.addCard(spell( 6,      EPIC, "Avenging Wrath").effect(repeat(fixed(8), toRandom(opponentPlayer().and(canTakeDamage(1)), damage(1)))).card());
		game.addCard(spell( 8,      EPIC, "Lay on Hands").effect(toAny(combined(heal(8), drawCards(3)))).card());
		game.addCard(weapon( 1,      FREE, 1, 4, "Light's Justice").card());
//		game.addCard(weapon( 4,    COMMON, 4, 2, "Truesilver Champion").effect("Whenever your hero attacks, restore 2 Health to it").card());
//		game.addCard(weapon( 3,      EPIC, 1, 5, "Sword of Justice").effect("Whenever you summon a minion, give it +1/+1 and this loses 1 Durability").card());
		game.addCard(weapon( 5, LEGENDARY, 5, 3, "Ashbringer").card());
	}

	private HStoneEffect drawCardAndDealDamageEqualToCost() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCard card = source.getPlayer().drawCard();
				int cost = card.getManaCost();
				damage(cost).performEffect(source, target);
			}
		};
	}

	private HSGetCount oppHandSizeMinusMyHandSize() {
		//   "Draw cards until you have as many in hand as your opponent"
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				int myHand = source.getPlayer().getHand().size(); // TODO: Do not count the card itself!
				int oppHand = source.getPlayer().getNextPlayer().getHand().size();
				return oppHand - myHand;
			}
		};
	}

	private HStoneEffect addEnchantOnAttackDrawCard() {
//		 TODO: "Choose a minion.  Whenever it attacks, draw a card"
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
//				target.addTrigger(new AttackTrigger());
			}
		};
	}

}
