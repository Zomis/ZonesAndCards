package net.zomis.cards.util;

public class ResourceType implements IResource {

	private final String	name;
	private final int	mDefault;

	public ResourceType(String name) {
		this(name, 0);
	}
	public ResourceType(String name, int defaultValue) {
		this.name = name;
		this.mDefault = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	@Override
	public ResourceData createData(IResource resource) {
		return ResourceData.forResource(resource, this.mDefault);
	}
	
}
