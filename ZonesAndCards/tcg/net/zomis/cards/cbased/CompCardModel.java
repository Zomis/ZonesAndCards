package net.zomis.cards.cbased;

import java.util.HashMap;
import java.util.Map;

import net.zomis.cards.components.Component;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;

public class CompCardModel extends CardModel implements HasComponents {

	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	
	public CompCardModel(String name) {
		super(name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <E extends CardModel> Card<E> createCardInternal(CardZone<?> initialZone) {
		Card<E> card = (Card<E>) new CardWithComponents<CompCardModel>(this, initialZone);
		return card;
	}

	@Override
	public Map<Class<? extends Component>, Component> getComponents() {
		return components;
	}

}
