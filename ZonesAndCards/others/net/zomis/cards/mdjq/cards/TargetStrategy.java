package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQObject;
import net.zomis.cards.mdjq.MDJQPermanent;

public interface TargetStrategy {
	boolean isValidTarget(MDJQPermanent card, MDJQObject object);
}
