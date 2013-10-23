package net.zomis.cards.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public final class ResourceMap {

	private final Map<IResource, ResourceData> data = new TreeMap<IResource, ResourceData>(new ResourceComparator());
	public static class ResourceComparator implements Comparator<IResource> {
		@Override
		public int compare(IResource o1, IResource o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}
	
	public Map<IResource, ResourceData> getData() {
		return new HashMap<IResource, ResourceData>(data);
	}
	
	public ResourceMap() {}
	public ResourceMap(ResourceMap copyOf) {
		for (Entry<IResource, ResourceData> ee : copyOf.data.entrySet()) {
			if (ee.getValue() == null)
				throw new NullPointerException("ee value null");
			this.set(ee.getKey(), ee.getValue().getRealValue());
		}
	}
	
	public ResourceData dataFor(IResource type) {
		ResourceData data = this.data.get(type);
		if (data == null) {
			data = type.createData(type);
			if (data == null || data.getResource() != type)
				throw new IllegalStateException("Resource did not return a correct ResourceData object: " + type);
			this.data.put(type, data);
		}
		return data;
	}
	
	public int getResources(IResource type) {
		return dataFor(type).getValueWithStrategy(this);
	}
	
	public boolean hasResources(IResource type, int amount) {
		return getResources(type) >= amount;
	}
	public void changeResources(IResource type, Integer value) {
		if (value == null) return;
		
		ResourceListener listener = getListener(type);
		if (listener != null && !listener.onResourceChange(this, dataFor(type), value)) {
			return;
		}
		ResourceData dat = dataFor(type);
		Integer val = dat.getRealValue();
		if (val == null) val = dat.getDefaultValue();
		set(type, val + value);
	}
	public ResourceListener getListener(IResource type) {
		return dataFor(type).getListener();
	}
	public ResourceMap setResourceStrategy(IResource type, ResourceStrategy strategy) {
		if (strategy == null)
			dataFor(type).strategy = null;
		else dataFor(type).strategy = strategy;
		return this;
	}
	public ResourceMap set(IResource type, Integer value) {
		dataFor(type).value = value;
		return this;
	}
	public Set<IResource> getKeys() {
		return new HashSet<IResource>(this.data.keySet());
	}
	public Set<Entry<IResource, Integer>> getValues() {
		Map<IResource, Integer> values = new LinkedHashMap<IResource, Integer>();
		for (Entry<IResource, ResourceData> ee : this.data.entrySet()) {
			values.put(ee.getKey(), ee.getValue().value);
		}
		return values.entrySet();
	}

	@Override
	public String toString() {
		return this.data.toString(); // or getValues().toString() ?
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
			if (ee.getValue() == null) continue;
			this.changeResources(ee.getKey(), ee.getValue() * multiplier);
		}
	}
	
	public void setListener(IResource type, ResourceListener listener) {
		dataFor(type).listener = listener;
	}

	public void clamp() {
		for (Entry<IResource, ResourceData> ee : this.data.entrySet()) {
			ee.getValue().clamp();
		}
	}
	
}
