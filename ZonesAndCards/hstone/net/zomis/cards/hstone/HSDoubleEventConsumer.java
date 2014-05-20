package net.zomis.cards.hstone;

import net.zomis.cards.hstone.events.HStoneDoubleCardEvent;

@FunctionalInterface
public interface HSDoubleEventConsumer {
	void handleEvent(HStoneCard listener, HStoneDoubleCardEvent event);
}
