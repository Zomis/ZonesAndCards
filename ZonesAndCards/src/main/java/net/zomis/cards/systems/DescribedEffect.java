package net.zomis.cards.systems;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.iface.CardEffectSingle;

public class DescribedEffect implements CardEffectSingle {

	private String str;
	private CardEffectSingle effect;

	public DescribedEffect(String str, CardEffectSingle effect) {
		this.str = str;
		this.effect = effect;
	}
	
	@Override
	public void performEffect(CardWithComponents card) {
		effect.performEffect(card);
	}
	
	@Override
	public String toString() {
		return str;
	}

}
