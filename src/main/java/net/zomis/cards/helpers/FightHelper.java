package net.zomis.cards.helpers;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.PTComponent;
import net.zomis.cards.events2.AttackEvent;
import net.zomis.cards.events2.CompGameEvent;
import net.zomis.cards.events2.DamageDealtEvent;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneRes;

public class FightHelper {

	public static void fight(CardWithComponents source, CardWithComponents target) {
		FirstCompGame game = source.getGame();
		
		game.executeEvent(new AttackEvent(source, target), () -> {
			int attack = source.getComponent(PTComponent.class).getPower();
			int counterAttack = target.getComponent(PTComponent.class).getPower();
			
			attack = damage(source, target, attack);
			counterAttack = damage(target, source, counterAttack);
			
			List<CompGameEvent> events = new ArrayList<>();
			if (attack > 0)
				events.add(new DamageDealtEvent(source, target, attack));
			if (counterAttack > 0)
				events.add(new DamageDealtEvent(target, source, counterAttack));
			
			game.cleanup(events);
		});
		
		// TODO: These things should be handled by post-event systems:
//		source.getResources().changeResources(HStoneRes.ACTION_POINTS_USED, 1);
//		source.removeAbility(HSAbility.STEALTH);
	}

	public static int damage(CardWithComponents source, CardWithComponents target, int damage) {
		if (target == null)
			throw new NullPointerException("A target has not been specified");
		
//		if (target.hasAbility(HSAbility.IMMUNE)) {
//			return 0;
//		}
//		
//		if (source.getModel().isType(CardType.SPELL)) {
//			int spellDamage = calcSpellDamageFor(source.getPlayer());
//			damage += spellDamage;
//		}

//		target.getResources().changeResources(HStoneRes.AWAITING_DAMAGE, damage);
		// TODO: Buffered Health system(?) - don't decrease health directly, do it during cleanup. Listen for CleanupEvent and HealthModificationEvent
		// TODO: DamageEvent to determine how much damage to apply (because of immune, spell damage, bonuses, enchantments, etc.)
		
		target.getRequiredComponent(HealthComponent.class).damage(damage);
		
		return damage;
	}
	
	public static void heal(HStoneCard target, int healing) {
		healing = Math.min(healing, target.getHealthMax() - target.getHealth());
		target.getResources().changeResources(HStoneRes.AWAITING_HEAL, healing);
	}

}
