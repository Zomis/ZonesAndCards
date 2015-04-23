package net.zomis.cards.components;

import net.zomis.cards.iface.CardEffectSingle;
import net.zomis.cards.iface.Component;
import net.zomis.cards.systems.DescribedEffect;

public class EffectComponent implements Component {

	private final CardEffectSingle	effect;

	public EffectComponent(String description, CardEffectSingle effect) {
		this(new DescribedEffect(description, effect));
	}
	
	public EffectComponent(CardEffectSingle effect) {
		if (effect == null)
			throw new NullPointerException();
		this.effect = effect;
	}
	
	public CardEffectSingle getEffect() {
		return effect;
	}
	
	@Override
	public String toString() {
		return effect.toString();
	}
	
}
