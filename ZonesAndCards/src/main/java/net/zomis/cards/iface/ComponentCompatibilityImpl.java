package net.zomis.cards.iface;

import java.util.HashSet;

import net.zomis.custommap.view.ZomisLog;

public class ComponentCompatibilityImpl implements ComponentCompatibility {

	private final HasComponents object;
	private final HashSet<Class<? extends Component>> missing;
	private int found;
	private int checked;

	public ComponentCompatibilityImpl(HasComponents object) {
		this.object = object;
		this.missing = new HashSet<>();
		this.found = 0;
		this.checked = 0;
	}
	
	@Override
	public ComponentCompatibility and(Class<? extends Component> class1) {
		boolean working = object.hasComponent(class1);
		if (working) {
			this.found += (working ? 1 : 0);
		}
		else this.missing.add(class1);
		this.checked++;
		return this;
	}

	@Override
	public boolean fails() {
		return this.found != this.checked;
	}

	@Override
	public void required() throws RuntimeException {
		if (fails()) {
			throw new RuntimeException("Missing components on " + object + ": found " + found + "/" + checked + " missing: " + missing);
		}
	}

	@Override
	public boolean failsThenWarn() {
		boolean result = fails();
		if (result) {
			ZomisLog.warn("Missing components on " + object + ": found " + found + "/" + checked + " missing: " + missing);
		}
		return result;
	}

}
