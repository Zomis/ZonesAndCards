package net.zomis.cards.cbased;

import java.util.HashMap;
import java.util.Map;

import net.zomis.cards.components.Component;
import net.zomis.cards.components.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;

public class CardWithComponents<M extends CardModel> extends Card<M> implements HasComponents {

	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	
	public CardWithComponents(M model, CardZone<?> initialZone) {
		super(model);
		this.currentZone = initialZone;
	}

	@Override
	public Map<Class<? extends Component>, Component> getComponents() {
		return components;
	}

}
