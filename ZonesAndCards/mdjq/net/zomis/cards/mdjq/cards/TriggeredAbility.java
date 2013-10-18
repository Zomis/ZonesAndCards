package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.events.MDJQEvent;

public interface TriggeredAbility {
	void trigger(MDJQPermanent triggeredWhere, MDJQEvent event);
}
