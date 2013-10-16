package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQEvent;

public interface TriggeredEffect {
	void apply(MDJQEvent event);
}
