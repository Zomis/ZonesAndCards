package net.zomis.cards.hstone.sets;

import static net.zomis.cards.hstone.factory.Battlecry.*;
import static net.zomis.cards.hstone.factory.HSFilters.*;
import static net.zomis.cards.hstone.factory.HStoneCardFactory.*;
import static net.zomis.cards.hstone.factory.HStoneRarity.*;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.hstone.triggers.DealDamageTrigger;
import net.zomis.cards.util.CardSet;

public class MageCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
//		game.addCard(minion( 1,    COMMON, 1, 3, "Mana Wyrm").effect("Whenever you cast a spell, gain +1 Attack").card());
		game.addCard(minion( 0,    COMMON, 0, 2, "Mirror Image -Minion").taunt().card());
//		game.addCard(minion( 2,    COMMON, 3, 2, "Sorcerer's Apprentice").effect("Your spells cost (1) less").card());
		game.addCard(minion( 4,    COMMON, 3, 6, "Water Elemental").on(new DealDamageTrigger(freezeDamagedMinion(), thisCard())).card());
//		game.addCard(minion( 4,      RARE, 3, 3, "Ethereal Arcanist").effect("If you control a").effect("<b>Secret</b>").effect("at the end of your turn, gain +2/+2").card());
//		game.addCard(minion( 3,      RARE, 4, 3, "Kirin Tor Mage").effect("<b>Battlecry:</b>").effect("The next").effect("<b>Secret</b>").effect("you play this turn costs (0)").card());
		game.addCard(minion( 0,      EPIC, 1, 3, "Spellbender").card());
//		game.addCard(minion( 7, LEGENDARY, 5, 7, "Archmage Antonidas").effect("Whenever you cast a spell, put a 'Fireball' spell into your hand").card());
		game.addCard(spell( 2,      FREE, "Arcane Explosion").effect(damageEnemyMinions(1)).card());
		game.addCard(spell( 3,      FREE, "Arcane Intellect").effect(drawCards(2)).card());
		game.addCard(spell( 1,      FREE, "Arcane Missiles").effect(repeat(fixed(3), toRandom(opponentPlayer().and(canTakeDamage(1)), damage(1)))).card());
		game.addCard(spell( 4,      FREE, "Fireball").effect(damage(6)).card());
//		game.addCard(spell( 4,      FREE, "Polymorph").effect("Transform a minion into a 1/1 Sheep").card());
//		game.addCard(spell( 4,    COMMON, "Cone of Cold").effect("<b>Freeze</b>").effect("a minion and the minions next to it, and deal 1 damage to them").card());
		game.addCard(spell( 7,    COMMON, "Flamestrike").effect(damageEnemyMinions(4)).card());
		game.addCard(spell( 3,    COMMON, "Frost Nova").effect(forEach(allMinions().and(not(samePlayer())), null, freeze())).card());
		game.addCard(spell( 2,    COMMON, "Frostbolt").effect(toAny(combined(damage(3), freeze()))).card());
//		game.addCard(spell( 3,    COMMON, "Ice Barrier").effect("<b>Secret:</b>").effect("As soon as your hero is attacked, gain 8 Armor").card());
		game.addCard(spell( 1,    COMMON, "Ice Lance").effect(freezeOrDamage(4)).card());
//		game.addCard(spell( 3,    COMMON, "Mirror Entity").effect("<b>Secret:</b>").effect("When your opponent plays a minion, summon a copy of it").card());
		game.addCard(spell( 1,    COMMON, "Mirror Image").effect(summon("Mirror Image -Minion", 2)).card());
		game.addCard(spell( 6,      RARE, "Blizzard").effect(forEach(opponentMinions(), null, combined(damage(2), freeze()))).card());
//		game.addCard(spell( 3,      RARE, "Counterspell").effect("<b>Secret:</b>").effect("When your opponent casts a spell,").effect("<b>Counter</b>").effect("it").card());
//		game.addCard(spell( 3,      RARE, "Vaporize").effect("<b>Secret:</b>").effect("When a minion attacks your hero, destroy it").card());
//		game.addCard(spell( 3,      EPIC, "Ice Block").effect("<b>Secret:</b>").effect("When your hero takes fatal damage, prevent it and become").effect("<b>Immune</b>").effect("this turn").card());
		game.addCard(spell(10,      EPIC, "Pyroblast").effect(damage(10)).card());
//		game.addCard(spell( 3,      EPIC, "Spellbender").effect("<b>Secret:</b>").effect("When an enemy casts a spell on a minion, summon a 1/3 as the new target").card());
	}

	private HStoneEffect freezeDamagedMinion() {
		return freeze();
	}



}
