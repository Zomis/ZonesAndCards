package net.zomis.cards.resources;

/**
 * A Strategy for reporting an amount of resources in a {@link ResourceMap}
 */
public interface ResourceStrategy {
	/**
	 * 
	 * @param type The resource data information to report the resources of
	 * @param map The resource map which requests the resource amount
	 * @return The number of resources that should be reported
	 */
	int getResourceAmount(ResourceData type, ResourceMap map);
}
