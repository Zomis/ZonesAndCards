package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneEffect;

public class DruidCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	public static final HStoneOption	EFFECT_INCREASE_MANA_TOTAL	= namedEffect("Gain 2 mana crystals", e.manaPermanentFilled(2));
	public static final HStoneOption	EFFECT_DRAW_THREE			= namedEffect("Draw three cards", e.drawCards(3));
	
	private static final HStoneOption	EFFECT_DAMAGE_5_TO_MINION	= namedEffect("Deal 5 damage to a minion", e.damage(5, f.allMinions()));
	private static final HStoneOption	EFFECT_DAMAGE_2_TO_ALL_ENEMY_MINIONS = namedEffect("Deal 2 damage to all enemy minions", e.forEach(f.opponentMinions(), null, e.damage(2)));
	
	private static final HStoneOption	DAMAGE_2 = namedEffect("Deal 2 damage", e.damage(2, f.all()));
	private static final HStoneOption	SILENCE	= namedEffect("Silence a minion", e.silencer());
	
	private static final HStoneOption	DRAW_2 = namedEffect("Draw 2 cards", e.drawCards(2));
	private static final HStoneOption	HEAL_5 = namedEffect("Restore 5 health", e.heal(5));

	private static final HStoneOption	ATTACK_5 = namedEffect("+5 attack", e.selfPT(5, 0));
	private static final HStoneOption	HEALTH_5_TAUNT = namedEffect("+5 health and taunt", e.combined(e.selfPT(0, 5), e.toSelf(e.giveAbility(HSAbility.TAUNT))));
	
	private static final HStoneOption	ATTACK_4 = namedEffect("+4 attack", e.selfPT(4, 0));
	private static final HStoneOption	HEALTH_4_TAUNT = namedEffect("+4 health and taunt", e.combined(e.selfPT(0, 4), e.toSelf(e.giveAbility(HSAbility.TAUNT))));
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 2, 2, "Treant1").card());
		game.addCard(minion( 1,      NONE, 2, 2, "Treant2").taunt().card());
//		game.addCard(minion( 5,    COMMON, 4, 6, "Druid of the Claw").taunt().card());
//		game.addCard(minion( 5,    COMMON, 4, 4, "Druid of the Claw").effect("<b>Choose One -</b>").effect("<b>Charge</b>").effect("; or +2 Health and").effect("<b>Taunt</b>").effect("").card());
//		game.addCard(minion( 5,    COMMON, 4, 4, "Druid of the Claw").charge().card());
		game.addCard(minion( 8,    COMMON, 8, 8, "Ironbark Protector").taunt().card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Panther").card());
		game.addCard(minion( 1,    COMMON, 2, 2, "Treant3").charge().on(HStoneTurnEndEvent.class, e.selfDestruct(), f.samePlayer()).card());
		game.addCard(minion( 4,      RARE, 2, 4, "Keeper of the Grove").battlecry(e.chooseOne(DAMAGE_2, SILENCE)).card());
		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of Lore").battlecry(e.chooseOne(DRAW_2, HEAL_5)).card());
		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of War").battlecry(e.chooseOne(ATTACK_5, HEALTH_5_TAUNT)).card());
//		game.addCard(minion( 9, LEGENDARY, 5, 8, "Cenarius").battlecry("<b>Choose One</b>").effect("- Give your other minions +2/+2; or summon('Treant2', 2)").effect("<b>Taunt</b>").effect("").card());
		game.addCard(spell( 0,      NONE, "Excess Mana").effect(e.drawCard()).card());
		game.addCard(spell( 1,      FREE, "Claw").effect(e.combined(e.tempBoostToMyHero(2, 0), e.armor(2))).card());
		game.addCard(spell( 3,      FREE, "Healing Touch").effect(e.heal(8)).card());
		game.addCard(spell( 0,      FREE, "Innervate").effect(e.tempMana(2)).card());
		game.addCard(spell( 2,      FREE, "Mark of the Wild").effect(e.toMinion(e.combined(e.giveAbility(HSAbility.TAUNT), e.otherPT(2, 2)))).card());
		game.addCard(spell( 2,      FREE, "Wild Growth").effect(e.manaPermanentEmpty(1)).card());
		game.addCard(spell( 3,    COMMON, "Mark of Nature").effect(e.iff(f.allMinions(), e.chooseOne(ATTACK_4, HEALTH_4_TAUNT))).card());
		game.addCard(spell( 0,    COMMON, "Moonfire").effect(e.damage(1)).card());
		game.addCard(spell( 1,    COMMON, "Naturalize").effect(e.toMinion(e.combined(e.destroyTarget(), e.oppDraw(2)))).card());
//		game.addCard(spell( 2,    COMMON, "Power of the Wild").effect("<b>Choose One</b>").effect("- Give your minions +1/+1; or Summon a 3/2 Panther").card());
		game.addCard(spell( 3,    COMMON, "Savage Roar").effect(e.forEach(f.samePlayer(), null, e.tempBoost(f.all(), 2, 0))).card());
//		game.addCard(spell( 4,    COMMON, "Soul of the Forest").effect("Give your minions ").effect("<b>Deathrattle:</b>").effect("Summon a 2/2 Treant.").card());
		game.addCard(spell( 6,    COMMON, "Starfire").effect(e.toAny(e.combined(e.damage(5), e.drawCard()))).card());
		game.addCard(spell( 4,    COMMON, "Swipe").effect(e.fourDamageToAnEnemyAnd1DamageToOtherEnemies()).card());
//		game.addCard(spell( 2,    COMMON, "Wrath").effect("<b>Choose One</b>").effect("- Deal 3 damage to a minion; or 1 damage and draw a card").card());
		game.addCard(spell( 4,      RARE, "Bite").effect(e.combined(e.tempBoostToMyHero(4, 0), e.armor(4))).card());
		game.addCard(spell( 5,      RARE, "Nourish").effect(e.chooseOne(EFFECT_INCREASE_MANA_TOTAL, EFFECT_DRAW_THREE)).card());
		game.addCard(spell( 1,      RARE, "Savagery").effect(e.damage(c.myHeroAttack, f.allMinions())).card());
		game.addCard(spell( 5,      RARE, "Starfall").effect(e.chooseOne(EFFECT_DAMAGE_5_TO_MINION, EFFECT_DAMAGE_2_TO_ALL_ENEMY_MINIONS)).card());
		game.addCard(spell( 6,      EPIC, "Force of Nature").effect(e.iff(f.haveSpaceOnBattleField, e.summon("Treant3", 3))).card());
	}

	public static HStoneOption namedEffect(String string, HStoneEffect changeMyManaTotal) {
		return new HStoneOption(string, changeMyManaTotal);
	}


}
