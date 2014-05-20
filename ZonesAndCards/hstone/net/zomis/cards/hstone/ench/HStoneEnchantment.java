package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.HSAbility;

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
