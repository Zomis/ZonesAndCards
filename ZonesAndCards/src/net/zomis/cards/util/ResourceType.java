package net.zomis.cards.util;

public class ResourceType implements IResource {

	private String	name;
	private int	min;
	private int	max;
	private int	mDefault;

	public ResourceType(String name) {
		this(name, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
	}
	public ResourceType(String name, int min, int max, int defaultValue) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.mDefault = defaultValue;
	}
	
	@Override
	public int getMax() {
		return max;
	}
	@Override
	public int getMin() {
		return min;
	}
	@Override
	public int getDefault() {
		return mDefault;
	}
	
	public ResourceType setMax(int max) {
		this.max = max;
		return this;
	}
	public ResourceType setMin(int min) {
		this.min = min;
		return this;
	}
	public ResourceType setDefault(int mDefault) {
		this.mDefault = mDefault;
		return this;
	}
	public IResource unmodifiable() {
		return new UnmodifiableResource(this.name, this.getMin(), this.getMax(), this.getDefault());
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
