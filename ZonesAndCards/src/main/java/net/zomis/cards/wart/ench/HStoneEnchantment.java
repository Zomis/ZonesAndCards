package net.zomis.cards.wart.ench;

import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.factory.HSAbility;

public abstract class HStoneEnchantment {
	
	public abstract boolean appliesTo(HStoneCard card);
	
	public boolean hasAbility(HStoneCard card, HSAbility ability, boolean hasAbility) {
		return hasAbility;
	}
	
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		return resources;
	}
	
	public boolean isActive() {
		return true;
	}
	
}
