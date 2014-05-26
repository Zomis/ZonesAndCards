package net.zomis.cards.components;

import java.util.Map;

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

}
