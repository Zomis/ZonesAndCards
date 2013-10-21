package net.zomis.cards.util;

public class ResourceType implements IResource {

	private String	name;
	private int	mDefault;

	public ResourceType(String name) {
		this(name, 0);
	}
	public ResourceType(String name, int defaultValue) {
		this.name = name;
		this.mDefault = defaultValue;
	}
	
	@Override
	public int getDefault() {
		return mDefault;
	}
	public ResourceType setDefault(int mDefault) {
		this.mDefault = mDefault;
		return this;
	}
	public IResource unmodifiable() {
		return new UnmodifiableResource(this.name, this.getDefault());
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
