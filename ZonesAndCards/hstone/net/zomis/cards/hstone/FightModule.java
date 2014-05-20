package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.events.HStoneDamageDealtEvent;
import net.zomis.cards.hstone.events.HStonePreAttackEvent;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.events.IEvent;

public class FightModule {

	public static void attack(HStoneGame game, HStoneCard source, HStoneCard target) {
		int attack = source.getAttack();
		int counterAttack = target.getAttack();
		
		if (game.callEvent(new HStonePreAttackEvent(source, target)).isCancelled()) {
			return;
		}
		
		attack = damage(source, target, attack);
		counterAttack = damage(target, source, counterAttack);
		source.getResources().changeResources(HStoneRes.ACTION_POINTS_USED, 1);
		
		List<IEvent> events = new ArrayList<IEvent>();
		if (attack > 0)
			events.add(new HStoneDamageDealtEvent(source, target));
		if (counterAttack > 0)
			events.add(new HStoneDamageDealtEvent(target, source));
		
		game.cleanup(events);
	}

	public static int damage(HStoneCard source, HStoneCard target, int damage) {
		if (target.hasAbility(HSAbility.IMMUNE)) {
			return 0;
		}
		if (source.getModel().isType(CardType.SPELL)) {
			int spellDamage = calcSpellDamageFor(source.getPlayer());
			damage += spellDamage;
		}
		
		target.getResources().changeResources(HStoneRes.AWAITING_DAMAGE, damage);
		return damage;
	}
	
	private static int calcSpellDamageFor(HStonePlayer player) {
		int spellDamage = 0;
		for (HStoneCard card : player.getBattlefield()) {
			spellDamage += card.getResources().getResources(HStoneRes.SPELL_DAMAGE);
		}
		
		return spellDamage;
	}

	public static void heal(HStoneCard target, int healing) {
		healing = Math.min(healing, target.getHealthMax() - target.getHealth());
		target.getResources().changeResources(HStoneRes.AWAITING_HEAL, healing);
	}

}
