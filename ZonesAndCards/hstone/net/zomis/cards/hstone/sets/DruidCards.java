package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;

import java.util.List;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.factory.Battlecry.HSGetCount;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.util.CardSet;

public class DruidCards implements CardSet<HStoneGame> {

	public static final HStoneOption	EFFECT_INCREASE_MANA_TOTAL	=  namedEffect("Gain 2 mana crystals", manaPermanentFilled(2));
	public static final HStoneOption	EFFECT_DRAW_THREE			=  namedEffect("Draw three cards", drawCards(3));

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
//		game.addCard(minion( 4,      RARE, 2, 4, "Keeper of the Grove").battlecry("<b>Choose One</b>").effect("- Deal 2 damage; or").effect("<b>Silence</b>").effect("a minion").card());
//		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of Lore").battlecry("<b>Choose One -</b>").effect("Draw 2 cards; or Restore 5 Health").card());
//		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of War").battlecry("<b>Choose One</b>").effect("-\n+5 Attack; or +5 Health and").effect("<b>Taunt</b>").effect("").card());
//		game.addCard(minion( 9, LEGENDARY, 5, 8, "Cenarius").battlecry("<b>Choose One</b>").effect("- Give your other minions +2/+2; or summon('Treant2', 2)").effect("<b>Taunt</b>").effect("").card());
		game.addCard(spell( 0,      NONE, "Excess Mana").effect(drawCard()).card());
		game.addCard(spell( 1,      FREE, "Claw").effect(combined(tempBoostToMyHero(2, 0), armor(2))).card());
		game.addCard(spell( 3,      FREE, "Healing Touch").effect(heal(8)).card());
		game.addCard(spell( 0,      FREE, "Innervate").effect(tempMana(2)).card());
		game.addCard(spell( 2,      FREE, "Mark of the Wild").effect(toMinion(combined(giveAbility(HSAbility.TAUNT), otherPT(2, 2)))).card());
		game.addCard(spell( 2,      FREE, "Wild Growth").effect(manaPermanentEmpty(1)).card());
//		game.addCard(spell( 3,    COMMON, "Mark of Nature").effect("<b>Choose One</b>").effect("- Give a minion +4 Attack; or +4 Health and").effect("<b>Taunt</b>").effect("").card());
		game.addCard(spell( 0,    COMMON, "Moonfire").effect(damage(1)).card());
		game.addCard(spell( 1,    COMMON, "Naturalize").effect(toMinion(combined(destroyTarget(), oppDraw(2)))).card());
//		game.addCard(spell( 2,    COMMON, "Power of the Wild").effect("<b>Choose One</b>").effect("- Give your minions +1/+1; or Summon a 3/2 Panther").card());
//		game.addCard(spell( 3,    COMMON, "Savage Roar").effect("Give your characters +2 Attack this turn").card());
//		game.addCard(spell( 4,    COMMON, "Soul of the Forest").effect("Give your minions ").effect("<b>Deathrattle:</b>").effect("Summon a 2/2 Treant.").card());
		game.addCard(spell( 6,    COMMON, "Starfire").effect(toAny(combined(damage(5), drawCard()))).card());
		game.addCard(spell( 4,    COMMON, "Swipe").effect(fourDamageToAnEnemyAnd1DamageToOtherEnemies()).card());
//		game.addCard(spell( 2,    COMMON, "Wrath").effect("<b>Choose One</b>").effect("- Deal 3 damage to a minion; or 1 damage and draw a card").card());
		game.addCard(spell( 4,      RARE, "Bite").effect(combined(tempBoostToMyHero(4, 0), armor(4))).card());
		game.addCard(spell( 5,      RARE, "Nourish").effect(chooseOne(EFFECT_INCREASE_MANA_TOTAL, EFFECT_DRAW_THREE)).card());
		game.addCard(spell( 1,      RARE, "Savagery").effect(damage(myHeroAttack(), allMinions())).card());
//		game.addCard(spell( 5,      RARE, "Starfall").effect("<b>Choose One -</b>").effect("Deal 5 damage to a minion; or 2 damage to all enemy minions").card());
		game.addCard(spell( 6,      EPIC, "Force of Nature").effect(summon("Treant3", 3)).card());
	}

	public static HSGetCount myHeroAttack() {
		return (src, dst) -> src.getPlayer().getPlayerCard().getAttack();
	}

	private static HStoneOption namedEffect(String string, HStoneEffect changeMyManaTotal) {
		return new HStoneOption(string, changeMyManaTotal);
	}

	public static HStoneEffect chooseOne(HStoneOption... options) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				for (HStoneOption option : options) {
//					source.getGame().getTemporaryZone().
				}
			}
		};
	}

	public static HStoneEffect fourDamageToAnEnemyAnd1DamageToOtherEnemies() {
		final HStoneEffect damage4 = damage(4);
		final HStoneEffect damage1 = damage(1);
		return new HStoneEffect(opponentPlayer()) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				damage4.performEffect(source, target);
				
				List<HStoneCard> others = target.getGame().findAll(target, samePlayer().and(anotherCard()));
				for (HStoneCard card : others) {
					damage1.performEffect(source, card);
				}
			}
		};
	}

}
