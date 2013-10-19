package net.zomis.cards.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ResourceMap {

	private final Map<IResource, Integer> map = new LinkedHashMap<IResource, Integer>();
	private final Map<IResource, ResourceStrategy> strategies = new HashMap<IResource, ResourceStrategy>();
	private final Map<IResource, ResourceListener> listeners = new HashMap<IResource, ResourceListener>();
	
	public ResourceMap() {}
	public ResourceMap(ResourceMap copyOf) {
		for (Entry<IResource, Integer> ee : copyOf.map.entrySet()) {
			this.set(ee.getKey(), ee.getValue());
		}
	}
	
	public int getResources(IResource type) {
		ResourceStrategy strat = this.strategies.get(type);
		if (strat != null) {
			return strat.getResourceAmount(type, this);
		}
		
		Integer i = map.get(type);
		return i == null ? type.getDefault() : i;
	}
	
	public boolean hasResources(IResource type, int amount) {
		return getResources(type) >= amount;
	}
	public void changeResources(IResource type, int value) {
		ResourceListener listener = getListener(type);
		if (listener != null && !this.getListener(type).onResourceChange(this, type, value)) {
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
		if (strategy == null) {
			this.strategies.remove(type);
		}
		else {
			this.strategies.put(type, strategy);
		}
		return this;
	}
	public ResourceMap set(IResource type, int value) {
		int newVal = value;
		int max = type.getMax();
		int min = type.getMin();
		if (newVal > max) newVal = max;
		if (newVal < min) newVal = min;
		this.map.put(type, value);
		return this;
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
	public void change(ResourceMap modifications, int multiplier) {
		for (Entry<IResource, Integer> ee : modifications.getValues()) {
			this.changeResources(ee.getKey(), ee.getValue() * multiplier);
		}
	}
	public void setListener(IResource type, ResourceListener listener) {
		this.listeners.put(type, listener);
	}
	
	public Map<IResource, Integer> getMapData() {
		return new HashMap<IResource, Integer>(map);
	}
	
}
