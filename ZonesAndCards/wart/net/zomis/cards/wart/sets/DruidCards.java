package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.Battlecry.*;
import static net.zomis.cards.wart.factory.HSFilters.*;
import static net.zomis.cards.wart.factory.HSGetCounts.*;
import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneEffect;

public class DruidCards implements CardSet<HStoneGame> {

	public static final HStoneOption	EFFECT_INCREASE_MANA_TOTAL	= namedEffect("Gain 2 mana crystals", manaPermanentFilled(2));
	public static final HStoneOption	EFFECT_DRAW_THREE			= namedEffect("Draw three cards", drawCards(3));
	
	private static final HStoneOption	EFFECT_DAMAGE_5_TO_MINION	= namedEffect("Deal 5 damage to a minion", damage(5, allMinions()));
	private static final HStoneOption	EFFECT_DAMAGE_2_TO_ALL_ENEMY_MINIONS = namedEffect("Deal 2 damage to all enemy minions", forEach(opponentMinions(), null, damage(2)));
	
	private static final HStoneOption	DAMAGE_2 = namedEffect("Deal 2 damage", damage(2, all()));
	private static final HStoneOption	SILENCE	= namedEffect("Silence a minion", silencer());
	
	private static final HStoneOption	DRAW_2 = namedEffect("Draw 2 cards", drawCards(2));
	private static final HStoneOption	HEAL_5 = namedEffect("Restore 5 health", heal(5));

	private static final HStoneOption	ATTACK_5 = namedEffect("+5 attack", selfPT(5, 0));
	private static final HStoneOption	HEALTH_5_TAUNT = namedEffect("+5 health and taunt", combined(selfPT(0, 5), toSelf(giveAbility(HSAbility.TAUNT))));
	
	private static final HStoneOption	ATTACK_4 = namedEffect("+4 attack", selfPT(4, 0));
	private static final HStoneOption	HEALTH_4_TAUNT = namedEffect("+4 health and taunt", combined(selfPT(0, 4), toSelf(giveAbility(HSAbility.TAUNT))));
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 2, "Treant1").card());
		game.addCard(minion( 1,      NONE, 2, 2, "Treant2").taunt().card());
//		game.addCard(minion( 5,    COMMON, 4, 6, "Druid of the Claw").taunt().card());
//		game.addCard(minion( 5,    COMMON, 4, 4, "Druid of the Claw").effect("<b>Choose One -</b>").effect("<b>Charge</b>").effect("; or +2 Health and").effect("<b>Taunt</b>").effect("").card());
//		game.addCard(minion( 5,    COMMON, 4, 4, "Druid of the Claw").charge().card());
		game.addCard(minion( 8,    COMMON, 8, 8, "Ironbark Protector").taunt().card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Panther").card());
		game.addCard(minion( 1,    COMMON, 2, 2, "Treant3").charge().on(HStoneTurnEndEvent.class, selfDestruct(), samePlayer()).card()); // TODO: *effect* not battlecry! Effect should also be applied when not played from hand.
		game.addCard(minion( 4,      RARE, 2, 4, "Keeper of the Grove").battlecry(chooseOne(DAMAGE_2, SILENCE)).card());
		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of Lore").battlecry(chooseOne(DRAW_2, HEAL_5)).card());
		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of War").battlecry(chooseOne(ATTACK_5, HEALTH_5_TAUNT)).card());
//		game.addCard(minion( 9, LEGENDARY, 5, 8, "Cenarius").battlecry("<b>Choose One</b>").effect("- Give your other minions +2/+2; or summon('Treant2', 2)").effect("<b>Taunt</b>").effect("").card());
		game.addCard(spell( 0,      NONE, "Excess Mana").effect(drawCard()).card());
		game.addCard(spell( 1,      FREE, "Claw").effect(combined(tempBoostToMyHero(2, 0), armor(2))).card());
		game.addCard(spell( 3,      FREE, "Healing Touch").effect(heal(8)).card());
		game.addCard(spell( 0,      FREE, "Innervate").effect(tempMana(2)).card());
		game.addCard(spell( 2,      FREE, "Mark of the Wild").effect(toMinion(combined(giveAbility(HSAbility.TAUNT), otherPT(2, 2)))).card());
		game.addCard(spell( 2,      FREE, "Wild Growth").effect(manaPermanentEmpty(1)).card());
		game.addCard(spell( 3,    COMMON, "Mark of Nature").effect(iff(allMinions(), chooseOne(ATTACK_4, HEALTH_4_TAUNT))).card());
		game.addCard(spell( 0,    COMMON, "Moonfire").effect(damage(1)).card());
		game.addCard(spell( 1,    COMMON, "Naturalize").effect(toMinion(combined(destroyTarget(), oppDraw(2)))).card());
//		game.addCard(spell( 2,    COMMON, "Power of the Wild").effect("<b>Choose One</b>").effect("- Give your minions +1/+1; or Summon a 3/2 Panther").card());
		game.addCard(spell( 3,    COMMON, "Savage Roar").effect(forEach(samePlayer(), null, tempBoost(all(), 2, 0))).card());
//		game.addCard(spell( 4,    COMMON, "Soul of the Forest").effect("Give your minions ").effect("<b>Deathrattle:</b>").effect("Summon a 2/2 Treant.").card());
		game.addCard(spell( 6,    COMMON, "Starfire").effect(toAny(combined(damage(5), drawCard()))).card());
		game.addCard(spell( 4,    COMMON, "Swipe").effect(fourDamageToAnEnemyAnd1DamageToOtherEnemies()).card());
//		game.addCard(spell( 2,    COMMON, "Wrath").effect("<b>Choose One</b>").effect("- Deal 3 damage to a minion; or 1 damage and draw a card").card());
		game.addCard(spell( 4,      RARE, "Bite").effect(combined(tempBoostToMyHero(4, 0), armor(4))).card());
		game.addCard(spell( 5,      RARE, "Nourish").effect(chooseOne(EFFECT_INCREASE_MANA_TOTAL, EFFECT_DRAW_THREE)).card());
		game.addCard(spell( 1,      RARE, "Savagery").effect(damage(myHeroAttack, allMinions())).card());
		game.addCard(spell( 5,      RARE, "Starfall").effect(chooseOne(EFFECT_DAMAGE_5_TO_MINION, EFFECT_DAMAGE_2_TO_ALL_ENEMY_MINIONS)).card());
		game.addCard(spell( 6,      EPIC, "Force of Nature").effect(iff(haveSpaceOnBattleField, summon("Treant3", 3))).card());
	}

	public static HStoneOption namedEffect(String string, HStoneEffect changeMyManaTotal) {
		return new HStoneOption(string, changeMyManaTotal);
	}


}
