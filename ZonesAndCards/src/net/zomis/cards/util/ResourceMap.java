package net.zomis.cards.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ResourceMap {

	private final Map<ResourceType, Integer> map = new LinkedHashMap<ResourceType, Integer>();
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	private int mDefault = 0;
	public void setMin(int min) {
		this.min = min;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	public ResourceMap() {
	}
	public ResourceMap(ResourceMap copyOf) {
		for (Entry<ResourceType, Integer> ee : copyOf.map.entrySet()) {
			this.set(ee.getKey(), ee.getValue());
		}
	}
	
	public int getResources(ResourceType type) {
		Integer i = map.get(type);
		return i == null ? mDefault : i;
	}
	
	public boolean hasResources(ResourceType type, int amount) {
		return getResources(type) >= amount;
	}
	public void changeResources(ResourceType type, int value) {
		Integer val = this.map.get(type);
		if (val == null) val = mDefault;
		set(type, val + value);
	}
	public ResourceMap set(ResourceType type, int value) {
		int newVal = value;
		if (newVal > max) newVal = max;
		if (newVal < min) newVal = min;
		this.map.put(type, value);
		return this;
	}
	public void setDefault(int i) {
		this.mDefault = i;
	}
	public Set<Entry<ResourceType, Integer>> getValues() {
		return this.map.entrySet();
	}

	@Override
	public String toString() {
		return this.map.toString();
	}
	public boolean hasResources(ResourceMap cost) {
		for (Entry<ResourceType, Integer> ee : cost.getValues()) {
			if (!this.hasResources(ee.getKey(), ee.getValue())) {
				return false;
			}
		}
		return true;
	}
	public void change(ResourceMap modifications, int multiplier) {
		for (Entry<ResourceType, Integer> ee : modifications.getValues()) {
			this.changeResources(ee.getKey(), ee.getValue() * multiplier);
		}
	}
}
