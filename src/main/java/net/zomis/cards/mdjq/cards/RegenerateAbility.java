package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQStackAction;
import net.zomis.cards.mdjq.activated.ActivatedAbility;
import net.zomis.cards.resources.ResourceMap;

public class RegenerateAbility implements ActivatedAbility {

//	private ResourceMap	cost;

	public RegenerateAbility(ResourceMap cost) {
//		this.cost = cost;
	}
	
	@Override
	public MDJQStackAction getAction(MDJQPermanent card) {
		// TODO Implement MDJQ RegenerateAbility
		return new MDJQStackAction(null);
	}

}
