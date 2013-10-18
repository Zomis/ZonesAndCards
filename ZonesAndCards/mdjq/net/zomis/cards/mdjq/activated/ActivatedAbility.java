package net.zomis.cards.mdjq.activated;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQStackAction;

public interface ActivatedAbility {

	MDJQStackAction getAction(MDJQPermanent card);
	
}
