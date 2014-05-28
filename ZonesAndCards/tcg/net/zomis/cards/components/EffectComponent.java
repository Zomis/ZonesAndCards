package net.zomis.cards.components;

import net.zomis.cards.iface.CardEffect;

public class EffectComponent implements Component {

	private final CardEffect	effect;

	public EffectComponent(CardEffect effect) {
		this.effect = effect;
	}
	
	public CardEffect getEffect() {
		return effect;
	}
	
}
