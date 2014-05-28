package net.zomis.cards.iface;

import net.zomis.cards.cbased.CardWithComponents;

@FunctionalInterface
public interface CardEffect {

	void performEffect(CardWithComponents card);
	
}
