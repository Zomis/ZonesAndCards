package net.zomis.cards.cbased;

import java.util.HashMap;
import java.util.Map;

import net.zomis.cards.iface.Component;
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
		Card<E> card = (Card<E>) new CardWithComponents(this, initialZone);
		return card;
	}

	@Override
	public Map<Class<? extends Component>, Component> getComponents() {
		return components;
	}
	
	public CompCardModel addComponents(Component... component) {
		for (Component comp : component) {
			addComponent(comp);
		}
		return this;
	}
	
	public CompCardModel addComponent(Component component) {
		if (component == null)
			throw new NullPointerException("Component cannot be null. Previously added components: " + components);
		this.components.put(component.getClass(), component);
		return this;
	}

}
