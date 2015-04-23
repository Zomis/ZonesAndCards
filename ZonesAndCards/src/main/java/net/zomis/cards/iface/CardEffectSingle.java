package net.zomis.cards.iface;

import net.zomis.cards.cbased.CardWithComponents;

@FunctionalInterface
public interface CardEffectSingle {

	void performEffect(CardWithComponents card);
	
}
