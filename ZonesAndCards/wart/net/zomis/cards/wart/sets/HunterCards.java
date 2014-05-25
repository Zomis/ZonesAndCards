package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.Battlecry.*;
import static net.zomis.cards.wart.factory.HSFilters.*;
import static net.zomis.cards.wart.factory.HSGetCounts.*;
import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;

import java.util.List;

import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.FightModule;
import net.zomis.cards.wart.HSDoubleEventConsumer;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.events.HStoneDoubleCardEvent;
import net.zomis.cards.wart.events.HStoneMinionDiesEvent;
import net.zomis.cards.wart.events.HStoneMinionSummonedEvent;
import net.zomis.cards.wart.events.HStonePreAttackEvent;
import net.zomis.cards.wart.events.HStoneSecretRevealedEvent;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.cards.wart.factory.HStoneMinionType;
import net.zomis.utils.ZomisList;

public class HunterCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,      NONE, 1, 1, "Hound").charge().card());
		game.addCard(minion( 4,      FREE, 4, 3, "Houndmaster").battlecry(toFriendlyBeast(combined(otherPT(2, 2), giveAbility(HSAbility.TAUNT)))).card());
		game.addCard(minion( 1,      FREE, 1, 1, "Timber Wolf").staticEffectOtherFriendlyBeastsBonus(1, 0).card());
		game.addCard(minion( 3,    COMMON, 4, 2, "Huffer").charge().card());
		game.addCard(minion( 3,    COMMON, 2, 4, "Leokk").staticPT(allMinions().and(samePlayer()).and(anotherCard()), 1, 0).card());
		game.addCard(minion( 3,    COMMON, 4, 4, "Misha").taunt().card());
		game.addCard(minion( 2,    COMMON, 2, 2, "Scavenging Hyena").on(HStoneMinionDiesEvent.class, selfPT(2, 1), minionIs(HStoneMinionType.BEAST).and(samePlayer())).card());
		game.addCard(minion( 0,    COMMON, 1, 1, "Snake").card());
		game.addCard(minion( 2,    COMMON, 2, 1, "Starving Buzzard").on(HStoneMinionSummonedEvent.class, drawCard(), minionIs(HStoneMinionType.BEAST).and(samePlayer())).card());
//		game.addCard(minion( 5,    COMMON, 2, 5, "Tundra Rhino").effect("Your Beasts have").effect("<b>Charge</b>").effect("").card());
		game.addCard(minion( 2,      RARE, 2, 2, "Hyena").card());
		game.addCard(minion( 6,      RARE, 6, 5, "Savannah Highmane").deathrattle(summon("Hyena", 2)).card());
		game.addCard(minion( 9, LEGENDARY, 8, 8, "King Krush").charge().card());
		game.addCard(spell( 1,      FREE, "Arcane Shot").effect(damage(2)).card());
		game.addCard(spell( 4,      FREE, "Multi-Shot").effect(iff(opponentHasMinions(2), toMultipleRandom(fixed(2), opponentMinions(), damage(3)))).card());
//		game.addCard(spell( 1,      FREE, "Tracking").effect("Look at the top three cards of your deck. Draw one and discard the others").card());
//		game.addCard(spell( 3,    COMMON, "Animal Companion").effect("Summon a random Beast Companion").card());
		game.addCard(spell( 3,    COMMON, "Deadly Shot").effect(toRandom(opponentMinions(), destroyTarget())).card());
//		game.addCard(spell( 2,    COMMON, "Explosive Trap").effect("<b>Secret:</b>").effect("When your hero is attacked, deal 2 damage to all enemies").card());
//		game.addCard(spell( 2,    COMMON, "Freezing Trap").effect("<b>Secret:</b>").effect("When an enemy minion attacks, return it to its owner's hand and it costs (2) more").card());
		game.addCard(spell( 0,    COMMON, "Hunter's Mark").effect(toMinion(set(HStoneRes.ATTACK, 1))).card());
		game.addCard(spell( 3,    COMMON, "Kill Command").effect(toAny(ifElse(haveBeast(), damage(5), damage(3)))).card());
//		game.addCard(spell( 2,    COMMON, "Snipe").effect("<b>Secret:</b>").effect("When your opponent plays a minion, deal 4 damage to it").card());
		game.addCard(spell( 3,    COMMON, "Unleash the Hounds").effect(iff(haveSpaceOnBattleField, forEach(opponentMinions(), summon("Hound"), null))).card());
		game.addCard(spell( 5,      RARE, "Explosive Shot").effect(toTargetAndAdjacents(all(), damage(5), damage(2))).card());
		game.addCard(spell( 1,      RARE, "Flare").effect(combined(forEach(allMinions(), null, remove(HSAbility.STEALTH)), destroyEnemySecrets(), drawCard())).card());
		game.addCard(spell( 2,      RARE, "Misdirection").secret(HStonePreAttackEvent.class, cancelAndAttackAnotherRandom(), null, targetIsMyHero()).card());
//		game.addCard(spell( 1,      EPIC, "Bestial Wrath").effect("Give a Beast +2 Attack and").effect("<b>Immune</b>").effect("this turn").card());
		game.addCard(spell( 2,      EPIC, "Snake Trap").secret(HStonePreAttackEvent.class, wrap(summon("Snake", 3)), null, samePlayer()).card());
		game.addCard(weapon( 3,      RARE, 3, 2, "Eaglehorn Bow").on(HStoneSecretRevealedEvent.class, selfPT(0, 1), all()).card()); //  effect("Whenever a").effect("<b>Secret</b>").effect("is revealed, gain +1 Durability"
//		game.addCard(weapon( 7,      EPIC, 5, 2, "Gladiator's Longbow").effect("Your hero is").effect("<b>Immune</b>").effect("while attacking").card());
	}

	private HSFilter targetIsMyHero() {
		return allPlayers().and(samePlayer());
	}

	private HSDoubleEventConsumer cancelAndAttackAnotherRandom() {
		//  effect("<b>Secret:</b>").effect("When a character attacks your hero, instead he attacks another random character"
		return new HSDoubleEventConsumer() {
			@Override
			public void handleEvent(HStoneCard listener, HStoneDoubleCardEvent event) {
				event.setCancelled(true);
				List<HStoneCard> newTargetOptions = listener.getGame().findAll(event.getTarget(), all());
				newTargetOptions.remove(event.getSource());
				newTargetOptions.remove(event.getTarget());
				HStoneCard newTarget = ZomisList.getRandom(newTargetOptions, listener.getGame().getRandom());
				if (newTarget == null)
					throw new AssertionError("No other characters available: " + listener + " event " + event.getSource() + " on target " + event.getTarget());
				FightModule.attack(listener.getGame(), event.getSource(), newTarget);
			}
		};
	}

	private HSDoubleEventConsumer wrap(HStoneEffect effect) {
		return new HSDoubleEventConsumer() {
			@Override
			public void handleEvent(HStoneCard listener, HStoneDoubleCardEvent event) {
				effect.performEffect(listener, event.getSource());
			}
		};
	}


}
