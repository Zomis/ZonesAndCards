package net.zomis.cards.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class ResourceMap {

	private final Map<IResource, Integer> map = new LinkedHashMap<IResource, Integer>();
	private final Map<IResource, ResourceStrategy> strategies = new HashMap<IResource, ResourceStrategy>();
	private final Map<IResource, ResourceListener> listeners = new HashMap<IResource, ResourceListener>();
	// TODO: Use Map<IResource, ResourceData> with a bunch of properties - can then re-add min and mix and stuff. Add method to create ResourceData to IResource interface?

	public Map<IResource, ResourceListener> getListeners() {
		return new HashMap<IResource, ResourceListener>(listeners);
	}
	public Map<IResource, ResourceStrategy> getStrategies() {
		return new HashMap<IResource, ResourceStrategy>(strategies);
	}
	
	public ResourceMap() {}
	public ResourceMap(ResourceMap copyOf) {
		for (Entry<IResource, Integer> ee : copyOf.map.entrySet()) {
			this.set(ee.getKey(), ee.getValue());
		}
	}
	
	public int getResources(IResource type) {
		ResourceStrategy strat = this.strategies.get(type);
		if (strat != null) {
			return strat.getResourceAmount(type, this); // Strategies can ignore max and min values
		}
		Integer i = map.get(type);
		return i == null ? type.getDefault() : clamp(type, i);
	}
	
	public boolean hasResources(IResource type, int amount) {
		return getResources(type) >= amount;
	}
	public void changeResources(IResource type, int value) {
		ResourceListener listener = getListener(type);
		if (listener != null && !listener.onResourceChange(this, type, value)) {
			return;
		}
		Integer val = this.map.get(type);
		if (val == null) val = type.getDefault();
		set(type, val + value);
	}
	public ResourceListener getListener(IResource type) {
		return listeners.get(type);
	}
	public ResourceMap setResourceStrategy(IResource type, ResourceStrategy strategy) {
		if (strategy == null)
			this.strategies.remove(type);
		else this.strategies.put(type, strategy);
		return this;
	}
	public ResourceMap set(IResource type, int value) {
		int newVal = clamp(type, value);
		this.map.put(type, newVal);
		return this;
	}
	@Deprecated
	public static int clamp(IResource type, int value) {
//		int max = type.getMax();
//		int min = type.getMin();
//		if (value > max) value = max;
//		if (value < min) value = min;
		return value;
	}
	public Set<Entry<IResource, Integer>> getValues() {
		return this.map.entrySet();
	}

	@Override
	public String toString() {
		return this.map.toString();
	}
	public boolean hasResources(ResourceMap cost) {
		for (Entry<IResource, Integer> ee : cost.getValues()) {
			if (!this.hasResources(ee.getKey(), ee.getValue())) {
				return false;
			}
		}
		return true;
	}
	@Deprecated
	public void change(ResourceMap modifications, int multiplier) {
		for (Entry<IResource, Integer> ee : modifications.getValues()) {
			this.changeResources(ee.getKey(), ee.getValue() * multiplier);
		}
	}
	public void setListener(IResource type, ResourceListener listener) {
		this.listeners.put(type, listener);
	}
	
}
