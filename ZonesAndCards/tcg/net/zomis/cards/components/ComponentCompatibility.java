package net.zomis.cards.components;

public interface ComponentCompatibility {

	ComponentCompatibility and(Class<? extends Component> class1);

	boolean fails();

	
	
	
}
