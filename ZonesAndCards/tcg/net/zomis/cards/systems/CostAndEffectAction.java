package net.zomis.cards.systems;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.components.CostComponent;
import net.zomis.cards.components.EffectComponent;
import net.zomis.cards.model.StackAction;

public class CostAndEffectAction extends StackAction {

	private final CardWithComponents card;

	public CostAndEffectAction(CardWithComponents card) {
		this.card = card;
	}
	
	@Override
	protected void onPerform() {
		CostComponent<?> cost = card.getModel().getComponent(CostComponent.class);
		if (cost != null)
			cost.perform(card);
		
		EffectComponent effect = card.getModel().getComponent(EffectComponent.class);
		if (effect != null)
			effect.getEffect().performEffect(card);
	}
	
	@Override
	public boolean actionIsAllowed() {
		CostComponent<?> cost = card.getModel().getComponent(CostComponent.class);
		return cost != null ? cost.test(card) : true;
	}
	
}
