package net.zomis.cards.mdjq.targets;

import net.zomis.cards.mdjq.MDJQObject;
import net.zomis.cards.mdjq.MDJQPlayer;
import net.zomis.cards.mdjq.actions.MDJQTargetAction;

public class TargetOpponent implements TargetStrategy {

	@Override
	public boolean isValidTarget(MDJQTargetAction action, MDJQObject object) {
		return object instanceof MDJQPlayer && object != action.getTargetChooser();
	}

}
