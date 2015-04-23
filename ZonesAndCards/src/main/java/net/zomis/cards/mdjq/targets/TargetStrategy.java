package net.zomis.cards.mdjq.targets;

import net.zomis.cards.mdjq.MDJQObject;
import net.zomis.cards.mdjq.actions.MDJQTargetAction;

public interface TargetStrategy {
	boolean isValidTarget(MDJQTargetAction action, MDJQObject object);
}
