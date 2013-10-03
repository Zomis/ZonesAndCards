package net.zomis.cards.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ResourceMap {

	private Map<ResourceType, Integer> map = new LinkedHashMap<>();
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	private int mDefault = 0;
	public void setMin(int min) {
		this.min = min;
	}
	public void setMax(int max) {
		this.max = max;
	}
	
	public ResourceMap() {}
	
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
	public void set(ResourceType type, int value) {
		int newVal = value;
		if (newVal > max) newVal = max;
		if (newVal < min) newVal = min;
		this.map.put(type, value);
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
}
