package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQStackAction;

public interface ActivatedAbility {

	MDJQStackAction getAction(MDJQPermanent card);
	
}
