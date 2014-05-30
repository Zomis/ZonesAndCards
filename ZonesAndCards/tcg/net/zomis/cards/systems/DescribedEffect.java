package net.zomis.cards.systems;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.iface.CardEffect;

public class DescribedEffect implements CardEffect {

	private String str;
	private CardEffect effect;

	public DescribedEffect(String str, CardEffect effect) {
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
