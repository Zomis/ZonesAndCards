package net.zomis.cards.iface;

import java.util.Map;

import net.zomis.cards.components.Component;
import net.zomis.cards.components.ComponentCompatibility;
import net.zomis.cards.components.ComponentCompatibilityImpl;

public interface HasComponents {

	@SuppressWarnings("unchecked")
	default <T extends Component> T getComponent(Class<T> clazz) {
		return (T) getComponents().get(clazz);
	}

	default boolean hasComponent(Class<? extends Component> class1) {
		return getComponents().containsKey(class1);
	}
	
	default ComponentCompatibility compatibility(Class<? extends Component> class1) {
		return new ComponentCompatibilityImpl(this).and(class1);
	}

	Map<Class<? extends Component>, Component> getComponents();

	default <T extends Component> T getRequiredComponent(Class<T> class1) {
		if (hasComponent(class1))
			return getComponent(class1);
		throw new NullPointerException("Required component not found: " + class1 + " in " + this);
	}
	
}
