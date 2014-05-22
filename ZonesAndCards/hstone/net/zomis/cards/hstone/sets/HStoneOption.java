package net.zomis.cards.hstone.sets;

import net.zomis.cards.hstone.factory.HStoneEffect;
import net.zomis.cards.model.CardModel;

public class HStoneOption extends CardModel {
	
	private final HStoneEffect effect;

	public HStoneOption(String name, HStoneEffect effect) {
		super(name);
		this.effect = effect;
	}

	public HStoneEffect getEffect() {
		return effect;
	}
	
}
