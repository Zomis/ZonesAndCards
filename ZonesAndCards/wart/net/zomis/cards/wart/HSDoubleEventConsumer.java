package net.zomis.cards.wart;

import net.zomis.cards.wart.events.HStoneDoubleCardEvent;

@FunctionalInterface
public interface HSDoubleEventConsumer {
	void handleEvent(HStoneCard listener, HStoneDoubleCardEvent event);
}
