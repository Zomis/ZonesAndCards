package net.zomis.cards.iface;


public interface ComponentCompatibility {

	ComponentCompatibility and(Class<? extends Component> class1);

	boolean fails();

	void required() throws RuntimeException;

	boolean failsThenWarn();
	
}
