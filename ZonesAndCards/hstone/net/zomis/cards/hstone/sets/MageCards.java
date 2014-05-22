package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HSGetCounts.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HSDoubleEventConsumer;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.events.HStoneCardPlayedEvent;
import net.zomis.cards.hstone.events.HStonePreAttackEvent;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.triggers.DealDamageTrigger;
import net.zomis.cards.util.CardSet;

public class MageCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,    COMMON, 1, 3, "Mana Wyrm").on(HStoneCardPlayedEvent.class, selfPT(1, 0), samePlayer().and(isSpell())).card());
		game.addCard(minion( 0,    COMMON, 0, 2, "Mirror Image -Minion").taunt().card());
//		game.addCard(minion( 2,    COMMON, 3, 2, "Sorcerer's Apprentice").effect("Your spells cost (1) less").card());
		game.addCard(minion( 4,    COMMON, 3, 6, "Water Elemental").on(new DealDamageTrigger(freezeDamagedMinion(), thisCard())).card());
		game.addCard(minion( 4,      RARE, 3, 3, "Ethereal Arcanist").on(HStoneTurnEndEvent.class, selfPT(2, 2), samePlayer().and(playerControlsSecret())).card());
//		game.addCard(minion( 3,      RARE, 4, 3, "Kirin Tor Mage").effect("<b>Battlecry:</b>").effect("The next").effect("<b>Secret</b>").effect("you play this turn costs (0)").card());
		game.addCard(minion( 0,      EPIC, 1, 3, "Spellbender").card());
		game.addCard(minion( 7, LEGENDARY, 5, 7, "Archmage Antonidas").on(HStoneCardPlayedEvent.class, giveCard("Fireball"), samePlayer().and(isSpell())).card());
		game.addCard(spell( 2,      FREE, "Arcane Explosion").effect(damageEnemyMinions(1)).card());
		game.addCard(spell( 3,      FREE, "Arcane Intellect").effect(drawCards(2)).card());
		game.addCard(spell( 1,      FREE, "Arcane Missiles").effect(repeat(fixed(3), toRandom(opponentPlayer().and(canTakeDamage(1)), damage(1)))).card());
		game.addCard(spell( 4,      FREE, "Fireball").effect(damage(6)).card());
		game.addCard(spell( 4,      FREE, "Polymorph").effect(transform("Sheep")).card());
		game.addCard(spell( 4,    COMMON, "Cone of Cold").effect(toAdjacentsAndTarget(combined(freeze(), damage(1)))).card());
		game.addCard(spell( 7,    COMMON, "Flamestrike").effect(damageEnemyMinions(4)).card());
		game.addCard(spell( 3,    COMMON, "Frost Nova").effect(forEach(opponentMinions(), null, freeze())).card());
		game.addCard(spell( 2,    COMMON, "Frostbolt").effect(toAny(combined(damage(3), freeze()))).card());
//		game.addCard(spell( 3,    COMMON, "Ice Barrier").effect("<b>Secret:</b>").effect("As soon as your hero is attacked, gain 8 Armor").card());
		game.addCard(spell( 1,    COMMON, "Ice Lance").effect(freezeOrDamage(4)).card());
		
		game.addCard(spell( 3,    COMMON, "Mirror Entity").secret(HStoneCardPlayedEvent.class, copyTarget(), opponentMinions().and(haveSpaceOnBattleField())).card());
		game.addCard(spell( 1,    COMMON, "Mirror Image").effect(iff(haveSpaceOnBattleField(), summon("Mirror Image -Minion", 2))).card());
		game.addCard(spell( 6,      RARE, "Blizzard").effect(forEach(opponentMinions(), null, combined(damage(2), freeze()))).card());
//		game.addCard(spell( 3,      RARE, "Counterspell").effect("<b>Secret:</b>").effect("When your opponent casts a spell,").effect("<b>Counter</b>").effect("it").card());
		game.addCard(spell( 3,      RARE, "Vaporize").secret(HStonePreAttackEvent.class, doubleEventConsumerSelfDestruct(), allMinions(), samePlayer().and(allPlayers())).card());
//		game.addCard(spell( 3,      EPIC, "Ice Block").effect("<b>Secret:</b>").effect("When your hero takes fatal damage, prevent it and become").effect("<b>Immune</b>").effect("this turn").card());
		game.addCard(spell(10,      EPIC, "Pyroblast").effect(damage(10)).card());
//		game.addCard(spell( 3,      EPIC, "Spellbender").effect("<b>Secret:</b>").effect("When an enemy casts a spell on a minion, summon a 1/3 as the new target").card());
	}

	private HSDoubleEventConsumer doubleEventConsumerSelfDestruct() {
		return (listener, event) -> event.getSource().destroy();
	}

	private HSFilter playerControlsSecret() {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return !searcher.getPlayer().getSecrets().isEmpty();
			}
		};
	}

	private HStoneEffect copyTarget() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.copyTo(source.getPlayer().getBattlefield());
			}
		};
	}

	@Deprecated
	public static HStoneEffect toAdjacentsAndTarget(HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				effect.performEffect(source, target);
				adjacents(effect).performEffect(source, target);
			}
		};
	}

	private HStoneEffect freezeDamagedMinion() {
		return freeze();
	}



}
