package net.zomis.cards.resources;

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
	
	private ResourceListener listener;
	private final boolean	useDefaults;
	
	public boolean isUseDefaults() {
		return useDefaults;
	}
	
	public void setGlobalListener(ResourceListener listener) {
		this.listener = listener;
	}
	
	public Map<IResource, ResourceData> getData() {
		return new HashMap<IResource, ResourceData>(data);
	}
	/**
	 * Creates a new, empty, ResourceMap without using default values for the resources.
	 */
	public ResourceMap() { this(false); }
	public ResourceMap(boolean useDefaults) {
		this.useDefaults = useDefaults;
	}
	/**
	 * Creates a new map with <b>values</b> copied from the specified map. No strategies or listeners are copied this way.
	 * This is the same as <code>ResourceMap(copyOf, false)</code>
	 * @param copyOf The {@link ResourceMap} to create a copy of.
	 */
	public ResourceMap(ResourceMap copyOf) {
		this(copyOf, false);
	}
	/**
	 * Creates a map without using defaults that is a copy of another map, with the option to also copy the exact {@link ResourceData} structure for each resource. 
	 * @param copyOf The map to create a copy of.
	 * @param copyDetailedInfo If true, also listeners and strategies etc. for all ResourceDatas will be copied.
	 */
	public ResourceMap(ResourceMap copyOf, boolean copyDetailedInfo) {
		this(false);
		if (copyOf != null)
		for (Entry<IResource, ResourceData> ee : copyOf.data.entrySet()) {
			if (copyDetailedInfo)
				this.data.put(ee.getKey(), new ResourceData(ee.getValue()));
			else {
				if (ee.getValue() == null)
					throw new NullPointerException("ee value null");
				this.set(ee.getKey(), ee.getValue().getRealValue());
			}
		}
	}
	
	/**
	 * Get the {@link ResourceData} object associated with the specified type for this map, or creates a new one if no previous data was found.
	 * @param type {@link IResource} to check for.
	 * @return {@link ResourceData} object for the specified type.
	 */
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
	public void changeResources(IResource type, int value) {
		ResourceListener listener = getListener(type);
		if (listener != null && !listener.onResourceChange(this, dataFor(type), value)) {
			return;
		}
		ResourceData dat = dataFor(type);
		Integer val = dat.getRealValue();
		if (val == null) val = this.isUseDefaults() ? dat.getDefaultValue() : 0;
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
		ResourceData resdata = dataFor(type);
		if (this.listener != null) {
			this.listener.onResourceChange(this, resdata, value);
		}
		resdata.value = value;
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
			if (ee.getValue() == null)
				continue;
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
