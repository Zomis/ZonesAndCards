package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;


public class PaladinCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      FREE, 1, 1, "Silver Hand Recruit").card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Argent Protector").battlecry(e.giveAbility(HSAbility.DIVINE_SHIELD)).card());
		game.addCard(minion( 1,    COMMON, 2, 1, "Defender").card());
		game.addCard(minion( 7,    COMMON, 5, 6, "Guardian of Kings").battlecry(e.healMyHero(6)).card());
		game.addCard(minion( 3,      RARE, 3, 3, "Aldor Peacekeeper").battlecry(e.setAttack(1)).card());
		game.addCard(minion( 8, LEGENDARY, 6, 6, "Tirion Fordring").shield().taunt().deathrattle(e.equip("Ashbringer")).card());
		game.addCard(spell( 1,      FREE, "Blessing of Might").effect(e.toMinion(e.otherPT(3, 0))).card());
		game.addCard(spell( 4,      FREE, "Hammer of Wrath").effect(e.to(f.all(), e.combined(e.drawCard(), e.damage(3)))).card());
		game.addCard(spell( 1,      FREE, "Hand of Protection").effect(e.toMinion(e.giveAbility(HSAbility.DIVINE_SHIELD))).card());
		game.addCard(spell( 2,      FREE, "Holy Light").effect(e.heal(6)).card());
		game.addCard(spell( 4,    COMMON, "Blessing of Kings").effect(e.toMinion(e.otherPT(4, 4))).card());
		game.addCard(spell( 1,    COMMON, "Blessing of Wisdom").effect(e.addEnchantOnAttackDrawCard()).card());
		game.addCard(spell( 4,    COMMON, "Consecration").effect(e.forEach(f.opponent(), null, e.damage(2))).card());
//		game.addCard(spell( 1,    COMMON, "Eye for an Eye").effect("<b>Secret:</b>").effect("When your hero takes damage, deal that much damage to the enemy hero").card());
		game.addCard(spell( 1,    COMMON, "Humility").effect(e.toMinion(e.setAttack(1))).card());
//		game.addCard(spell( 1,    COMMON, "Noble Sacrifice").effect("<b>Secret:</b>").effect("When an enemy attacks, summon a 2/1 Defender as the new target").card());
//		game.addCard(spell( 1,    COMMON, "Redemption").effect("<b>Secret:</b>").effect("When one of your minions dies, return it to life with 1 Health").card());
//		game.addCard(spell( 1,    COMMON, "Repentance").effect("<b>Secret:</b>").effect("When your opponent plays a minion, reduce its Health to 1").card());
//		game.addCard(spell( 5,      RARE, "Blessed Champion").effect( "Double a minion's Attack").card());
		game.addCard(spell( 3,      RARE, "Divine Favor").effect(e.repeat(c.oppHandSizeMinusMyHandSize(), e.drawCard())).card());
		game.addCard(spell( 2,      RARE, "Equality").effect(e.forEach(f.allMinions(), null, e.set(HStoneRes.HEALTH, 1))).card());
		game.addCard(spell( 5,      RARE, "Holy Wrath").effect(e.toAny(e.drawCardAndDealDamageEqualToCost())).card());
		game.addCard(spell( 6,      EPIC, "Avenging Wrath").effect(e.repeat(c.fixed(8), e.toRandom(f.opponent().and(f.canTakeDamage(1)), e.damage(1)))).card());
		game.addCard(spell( 8,      EPIC, "Lay on Hands").effect(e.toAny(e.combined(e.heal(8), e.drawCards(3)))).card());
		game.addCard(weapon( 1,      FREE, 1, 4, "Light's Justice").card());
//		game.addCard(weapon( 4,    COMMON, 4, 2, "Truesilver Champion").effect("Whenever your hero attacks, restore 2 Health to it").card());
//		game.addCard(weapon( 3,      EPIC, 1, 5, "Sword of Justice").effect("Whenever you summon a minion, give it +1/+1 and this loses 1 Durability").card());
		game.addCard(weapon( 5, LEGENDARY, 5, 3, "Ashbringer").card());
	}

}
