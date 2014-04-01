package net.zomis.cards.resources;

/**
 * Standard implementation of an {@link IResource}
 */
public class ResourceType implements IResource {

	private final String	name;
	private final int	mDefault;
	
	/**
	 * Instantiate with a name and a default value of zero
	 * @param name The name of the resource
	 */
	public ResourceType(String name) {
		this(name, 0);
	}
	
	/**
	 * Instantiate with a name and a default value
	 * @param name The name of the resource
	 * @param defaultValue Number of starting resources
	 */
	public ResourceType(String name, int defaultValue) {
		this.name = name;
		this.mDefault = defaultValue;
	}
	
	/**
	 * Get the name of the resource
	 * @return The resource name
	 */
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
