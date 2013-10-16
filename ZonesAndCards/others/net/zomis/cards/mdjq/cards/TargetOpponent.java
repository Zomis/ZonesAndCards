package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQObject;
import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQPlayer;

public class TargetOpponent implements TargetStrategy {

	@Override
	public boolean isValidTarget(MDJQPermanent card, MDJQObject object) {
		return object instanceof MDJQPlayer && object != card.getController();
	}

}
