package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;

public class HStoneEnchForward extends HStoneEnchantment {
	
	private final HStoneEnchantment	forward;

	public HStoneEnchForward(HStoneEnchantment forward) {
		this.forward = forward;
	}
	
	@Override
	public boolean isActive() {
		return forward.isActive();
	}
	
	@Override
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		return forward.getResource(card, resource, resources);
	}
	
	@Override
	public boolean appliesTo(HStoneCard card) {
		return forward.appliesTo(card);
	}

}
