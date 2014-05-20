package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.triggers.CardEventTrigger;
import net.zomis.cards.util.CardSet;

public class DruidCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
//		game.addCard(minion( 1,      NONE, 2, 2, "Treant").card());
//		game.addCard(minion( 1,      NONE, 2, 2, "Treant").taunt().card());
//		game.addCard(minion( 5,    COMMON, 4, 6, "Druid of the Claw").taunt().card());
//		game.addCard(minion( 5,    COMMON, 4, 4, "Druid of the Claw").effect("<b>Choose One -</b>").effect("<b>Charge</b>").effect("; or +2 Health and").effect("<b>Taunt</b>").effect("").card());
//		game.addCard(minion( 5,    COMMON, 4, 4, "Druid of the Claw").charge().card());
		game.addCard(minion( 8,    COMMON, 8, 8, "Ironbark Protector").taunt().card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Panther").card());
		game.addCard(minion( 1,    COMMON, 2, 2, "Treant").charge().effect(destroyAtEndOfTurn()).card()); // TODO: *effect* not battlecry! Effect should also be applied when not played from hand.
//		game.addCard(minion( 4,      RARE, 2, 4, "Keeper of the Grove").effect("<b>Choose One</b>").effect("- Deal 2 damage; or").effect("<b>Silence</b>").effect("a minion").card());
//		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of Lore").effect("<b>Choose One -</b>").effect("Draw 2 cards; or Restore 5 Health").card());
//		game.addCard(minion( 7,      EPIC, 5, 5, "Ancient of War").effect("<b>Choose One</b>").effect("-\n+5 Attack; or +5 Health and").effect("<b>Taunt</b>").effect("").card());
//		game.addCard(minion( 9, LEGENDARY, 5, 8, "Cenarius").effect("<b>Choose One</b>").effect("- Give your other minions +2/+2; or Summon two 2/2 Treants with").effect("<b>Taunt</b>").effect("").card());
		game.addCard(spell( 0,      NONE, "Excess Mana").effect(drawCard()).card());
		game.addCard(spell( 1,      FREE, "Claw").effect(combined(tempBoost(allPlayers().and(samePlayer()), 2, 0), armor(2))).card());
		game.addCard(spell( 3,      FREE, "Healing Touch").effect(heal(8)).card());
		game.addCard(spell( 0,      FREE, "Innervate").effect(tempMana(2)).card());
		game.addCard(spell( 2,      FREE, "Mark of the Wild").effect(toMinion(combined(giveAbility(HSAbility.TAUNT), otherPT(2, 2)))).card());
		game.addCard(spell( 2,      FREE, "Wild Growth").effect(emptyMana(1)).card());
//		game.addCard(spell( 3,    COMMON, "Mark of Nature").effect("<b>Choose One</b>").effect("- Give a minion +4 Attack; or +4 Health and").effect("<b>Taunt</b>").effect("").card());
		game.addCard(spell( 0,    COMMON, "Moonfire").effect(damage(1)).card());
		game.addCard(spell( 1,    COMMON, "Naturalize").effect(toMinion(combined(destroyTarget(), oppDraw(2)))).card());
//		game.addCard(spell( 2,    COMMON, "Power of the Wild").effect("<b>Choose One</b>").effect("- Give your minions +1/+1; or Summon a 3/2 Panther").card());
//		game.addCard(spell( 3,    COMMON, "Savage Roar").effect("Give your characters +2 Attack this turn").card());
//		game.addCard(spell( 4,    COMMON, "Soul of the Forest").effect("Give your minions ").effect("<b>Deathrattle:</b>").effect("Summon a 2/2 Treant.").card());
		game.addCard(spell( 6,    COMMON, "Starfire").effect(toAny(combined(damage(5), drawCard()))).card());
//		game.addCard(spell( 4,    COMMON, "Swipe").effect("Deal 4 damage to an enemy and 1 damage to all other enemies").card());
//		game.addCard(spell( 2,    COMMON, "Wrath").effect("<b>Choose One</b>").effect("- Deal 3 damage to a minion; or 1 damage and draw a card").card());
		game.addCard(spell( 4,      RARE, "Bite").effect(combined(tempBoost(allPlayers().and(samePlayer()), 4, 0), armor(4))).card());
//		game.addCard(spell( 5,      RARE, "Nourish").effect("<b>Choose One</b>").effect("- Gain 2 Mana Crystals; or Draw 3 cards").card());
//		game.addCard(spell( 1,      RARE, "Savagery").effect("Deal damage equal to your hero's Attack to a minion").card());
//		game.addCard(spell( 5,      RARE, "Starfall").effect("<b>Choose One -</b>").effect("Deal 5 damage to a minion; or 2 damage to all enemy minions").card());
//		game.addCard(spell( 6,      EPIC, "Force of Nature").effect("Summon three 2/2 Treants with").effect("<b>Charge</b>").effect("that die at the end of the turn").card());
	}

	private HStoneEffect oppDraw(final int i) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final HStoneCard opp = source.getPlayer().getNextPlayer().getPlayerCard();
				drawCards(i).performEffect(opp, opp);
			}
		};
	}

	public static HStoneEffect emptyMana(final int mana) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_TOTAL, mana);
			}
		};
	}

	private HStoneEffect destroyAtEndOfTurn() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final int turn = source.getGame().getTurnNumber();
				source.addTrigger(new CardEventTrigger(HStoneTurnEndEvent.class, destroy(source), new HSFilter() {
					@Override
					public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
						return searcher.getGame().getTurnNumber() == turn;
					}
				}));
			}
		};
	}

	protected HStoneEffect destroy(final HStoneCard card) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				card.destroy();
			}
		};
	}

}
