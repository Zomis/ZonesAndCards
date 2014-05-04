package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.factory.HSAbility;

public class HStoneEnchantment {
	
	public HStoneEnchantment() {
		
	}
	
	public boolean appliesTo(HStoneCard card) {
		return false;
	}
	
	@Deprecated
	public Boolean hasAbility(HStoneCard card, HSAbility ability, Boolean hasAbility) {
		return hasAbility;
	}
	
	public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
		return resources;
	}
	
	public boolean isActive() {
		return true;
	}
	
}
