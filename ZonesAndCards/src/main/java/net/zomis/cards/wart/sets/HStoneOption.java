package net.zomis.cards.wart.sets;

import net.zomis.cards.model.CardModel;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.factory.HStoneEffect;

public class HStoneOption extends CardModel {
	
	private final HStoneEffect effect;

	public HStoneOption(String name, HStoneEffect effect) {
		super(name);
		this.effect = effect;
	}

	public HStoneEffect getEffect() {
		return effect;
	}

	public boolean isAllowed(HStoneCard cardCausedOption) {
		return effect.hasAnyAvailableTargets(cardCausedOption);
	}
	
}
