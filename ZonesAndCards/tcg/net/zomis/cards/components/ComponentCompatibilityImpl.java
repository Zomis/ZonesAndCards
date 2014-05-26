package net.zomis.cards.components;

import net.zomis.custommap.view.ZomisLog;

public class ComponentCompatibilityImpl implements ComponentCompatibility {

	private final HasComponents object;
	private final int found;
	private final int checked;

	public ComponentCompatibilityImpl(HasComponents object) {
		this.object = object;
		this.found = 0;
		this.checked = 0;
	}
	
	private ComponentCompatibilityImpl(ComponentCompatibilityImpl previous, boolean working) {
		this.object = previous.object;
		this.found = previous.found + (working ? 1 : 0);
		this.checked = previous.checked + 1;
	}
	
	@Override
	public ComponentCompatibility and(Class<? extends Component> class1) {
		return new ComponentCompatibilityImpl(this, object.hasComponent(class1));
	}

	@Override
	public boolean fails() {
		ZomisLog.info("Compatibility check: " + found + "/" + checked);
		return this.found != this.checked;
	}

}
