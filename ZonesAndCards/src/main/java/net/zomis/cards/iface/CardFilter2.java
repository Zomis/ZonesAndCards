package net.zomis.cards.iface;

import net.zomis.cards.cbased.CardWithComponents;

@FunctionalInterface
public interface CardFilter2 {

	boolean test(CardWithComponents searcher, CardWithComponents target);
	
}
