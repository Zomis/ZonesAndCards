package net.zomis.cards.wart.sets;

import static net.zomis.cards.wart.factory.HStoneCardFactory.*;
import static net.zomis.cards.wart.factory.HStoneRarity.*;
import net.zomis.cards.util.CardSet;
import net.zomis.cards.wart.HSDoubleEventConsumer;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.events.HStoneCardPlayedEvent;
import net.zomis.cards.wart.events.HStonePreAttackEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.factory.Battlecry;
import net.zomis.cards.wart.factory.HSFilters;
import net.zomis.cards.wart.factory.HSGetCounts;
import net.zomis.cards.wart.factory.HStoneEffect;
import net.zomis.cards.wart.triggers.DealDamageTrigger;

public class MageCards implements CardSet<HStoneGame> {

	private static final HSGetCounts c = new HSGetCounts();
	private static final HSFilters f = new HSFilters();
	private static final Battlecry e = new Battlecry();
	
	@Override
	public void addCards(HStoneGame game) {
		game.addCard(minion( 1,    COMMON, 1, 3, "Mana Wyrm").on(HStoneCardPlayedEvent.class, e.selfPT(1, 0), f.samePlayer().and(f.isSpell())).card());
		game.addCard(minion( 0,    COMMON, 0, 2, "Mirror Image -Minion").taunt().card());
		game.addCard(minion( 2,    COMMON, 3, 2, "Sorcerer's Apprentice").staticMana(f.isSpell().and(f.samePlayer()), c.fixedChange(-1)).card());
		game.addCard(minion( 4,    COMMON, 3, 6, "Water Elemental").on(new DealDamageTrigger(freezeDamagedMinion(), f.thisCard())).card());
		game.addCard(minion( 4,      RARE, 3, 3, "Ethereal Arcanist").on(HStoneTurnEndEvent.class, e.selfPT(2, 2), f.samePlayer().and(f.playerControlsSecret())).card());
//		game.addCard(minion( 3,      RARE, 4, 3, "Kirin Tor Mage").effect("<b>Battlecry:</b>").effect("The next").effect("<b>Secret</b>").effect("you play this turn costs (0)").card());
		game.addCard(minion( 0,      EPIC, 1, 3, "Spellbender").card());
		game.addCard(minion( 7, LEGENDARY, 5, 7, "Archmage Antonidas").on(HStoneCardPlayedEvent.class, e.giveCard("Fireball"), f.samePlayer().and(f.isSpell())).card());
		game.addCard(spell( 2,      FREE, "Arcane Explosion").effect(e.damageEnemyMinions(1)).card());
		game.addCard(spell( 3,      FREE, "Arcane Intellect").effect(e.drawCards(2)).card());
		game.addCard(spell( 1,      FREE, "Arcane Missiles").effect(e.repeat(c.fixed(3), e.toRandom(f.opponent().and(f.canTakeDamage(1)), e.damage(1)))).card());
		game.addCard(spell( 4,      FREE, "Fireball").effect(e.damage(6)).card());
		game.addCard(spell( 4,      FREE, "Polymorph").effect(e.transform("Sheep")).card());
		game.addCard(spell( 4,    COMMON, "Cone of Cold").effect(e.toAdjacentsAndTarget(e.combined(e.freeze(), e.damage(1)))).card());
		game.addCard(spell( 7,    COMMON, "Flamestrike").effect(e.damageEnemyMinions(4)).card());
		game.addCard(spell( 3,    COMMON, "Frost Nova").effect(e.forEach(f.opponentMinions(), null, e.freeze())).card());
		game.addCard(spell( 2,    COMMON, "Frostbolt").effect(e.toAny(e.combined(e.damage(3), e.freeze()))).card());
//		game.addCard(spell( 3,    COMMON, "Ice Barrier").effect("<b>Secret:</b>").effect("As soon as your hero is attacked, gain 8 Armor").card());
		game.addCard(spell( 1,    COMMON, "Ice Lance").effect(e.freezeOrDamage(4)).card());
		
		game.addCard(spell( 3,    COMMON, "Mirror Entity").secret(HStoneCardPlayedEvent.class, copyTarget(), f.opponentMinions().and(f.haveSpaceOnBattleField)).card());
		game.addCard(spell( 1,    COMMON, "Mirror Image").effect(e.iff(f.haveSpaceOnBattleField, e.summon("Mirror Image -Minion", 2))).card());
		game.addCard(spell( 6,      RARE, "Blizzard").effect(e.forEach(f.opponentMinions(), null, e.combined(e.damage(2), e.freeze()))).card());
//		game.addCard(spell( 3,      RARE, "Counterspell").effect("<b>Secret:</b>").effect("When your opponent casts a spell,").effect("<b>Counter</b>").effect("it").card());
		game.addCard(spell( 3,      RARE, "Vaporize").secret(HStonePreAttackEvent.class, doubleEventConsumerSelfDestruct(), f.allMinions(), f.samePlayer().and(f.allPlayers())).card());
//		game.addCard(spell( 3,      EPIC, "Ice Block").effect("<b>Secret:</b>").effect("When your hero takes fatal damage, prevent it and become").effect("<b>Immune</b>").effect("this turn").card());
		game.addCard(spell(10,      EPIC, "Pyroblast").effect(e.damage(10)).card());
//		game.addCard(spell( 3,      EPIC, "Spellbender").effect("<b>Secret:</b>").effect("When an enemy casts a spell on a minion, summon a 1/3 as the new target").card());
	}

	private HSDoubleEventConsumer doubleEventConsumerSelfDestruct() {
		return (listener, event) -> event.getSource().destroy();
	}

	private HStoneEffect copyTarget() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.copyTo(source.getPlayer().getBattlefield());
			}
		};
	}

	private HStoneEffect freezeDamagedMinion() {
		return e.freeze();
	}



}
