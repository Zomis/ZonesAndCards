package net.zomis.cards.components;

import net.zomis.cards.iface.CardEffect;
import net.zomis.cards.systems.DescribedEffect;

public class EffectComponent implements Component {

	private final CardEffect	effect;

	public EffectComponent(String description, CardEffect effect) {
		this(new DescribedEffect(description, effect));
	}
	
	public EffectComponent(CardEffect effect) {
		if (effect == null)
			throw new NullPointerException();
		this.effect = effect;
	}
	
	public CardEffect getEffect() {
		return effect;
	}
	
	@Override
	public String toString() {
		return effect.toString();
	}
	
}
