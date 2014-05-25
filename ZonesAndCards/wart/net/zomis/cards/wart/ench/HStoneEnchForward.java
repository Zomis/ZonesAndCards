package net.zomis.cards.wart.ench;

import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.factory.HSAbility;

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
	public boolean hasAbility(HStoneCard card, HSAbility ability, boolean hasAbility) {
		return forward.hasAbility(card, ability, hasAbility);
	}
	
	@Override
	public boolean appliesTo(HStoneCard card) {
		return forward.appliesTo(card);
	}

}
