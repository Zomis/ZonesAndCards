package net.zomis.cards.resources;

/**
 * Data for storing Resource information associated with a {@link ResourceMap}<br>
 * Modifications to an instance of this is normally done through a {@link ResourceMap}.
 * @author Zomis
 */
public class ResourceData {
	private final IResource resource;
	
	Integer value;
	ResourceListener listener;
	ResourceStrategy strategy;
	int min = Integer.MIN_VALUE;
	int max = Integer.MAX_VALUE;
	int defaultValue = 0;
	
	ResourceData() { this((IResource) null); }
	ResourceData(IResource res) {
		this.resource = res;
	}
	public ResourceData(ResourceData copy) {
		this.value = copy.value;
		this.resource = copy.resource;
		this.listener = copy.listener;
		this.strategy = copy.strategy;
		this.min = copy.min;
		this.max = copy.max;
		this.defaultValue = copy.defaultValue;
	}
	public static ResourceData forResource(IResource res) {
		return forResource(res, 0);
	}
	public static ResourceData forResource(IResource res, int defaultValue) {
		return forResource(res, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	public static ResourceData forResource(IResource res, int defaultValue, int min, int max) {
		ResourceData data = new ResourceData(res);
		data.defaultValue = defaultValue;
		data.min = min;
		data.max = max;
		return data;
	}
	
	public IResource getResource() {
		return resource;
	}
	public Integer getRealValue() {
		return value;
	}
	public ResourceListener getListener() {
		return listener;
	}
	public Integer getMax() {
		return max;
	}
	public Integer getMin() {
		return min;
	}
	public ResourceStrategy getStrategy() {
		return strategy;
	}
	public void clamp() {
		if (this.value == null)
			return;
		if (this.value > this.max)
			this.value = this.max;
		if (this.value < this.min)
			this.value = this.min;
	}
	public int getDefaultValue() {
		return this.defaultValue;
	}
	@Override
	public String toString() {
		return String.valueOf(this.value);
	}
	public int getRealValueOrDefault() {
		return value == null ? defaultValue : value;
	}
	int getValueWithStrategy(ResourceMap map) {
		ResourceStrategy strat = this.getStrategy();
		if (strat != null) {
			return strat.getResourceAmount(this, map); // Strategies can ignore max and min values
		}
		Integer i = this.getRealValue();
		return (i == null ? (map.isUseDefaults() ? this.getDefaultValue() : 0) : i);
	}
}
