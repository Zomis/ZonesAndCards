package net.zomis.cards.iface;

import net.zomis.cards.cbased.CardWithComponents;

@FunctionalInterface
public interface CardEffectTargets {

	void performEffect(CardWithComponents source, TargetData targets);
	
}
