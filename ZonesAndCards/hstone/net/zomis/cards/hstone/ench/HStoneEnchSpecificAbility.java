package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.factory.HSAbility;

public class HStoneEnchSpecificAbility extends HStoneEnchantment {

	private HStoneCard	target;
	private HSAbility	ability;

	public HStoneEnchSpecificAbility(HStoneCard target, HSAbility ability) {
		this.target = target;
		this.ability = ability;
	}
	
	@Override
	public boolean appliesTo(HStoneCard card) {
		return target == card;
	}
	
	@Override
	public boolean hasAbility(HStoneCard card, HSAbility ability, boolean hasAbility) {
		if (ability == this.ability)
			return true;
		return hasAbility;
	}

}
