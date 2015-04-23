package net.zomis.cards.resources;

/**
 * Listener for when resources change in a {@link ResourceMap}
 */
public interface ResourceListener {
	/**
	 * Method that is called when resources change in a map
	 * @param map The {@link ResourceMap} in which the change is happening
	 * @param type The associated {@link ResourceData} for the resource that is changing
	 * @param newValue The value that the resource is changing to
	 * @return True to allow the change, false to prevent it
	 */
	boolean onResourceChange(ResourceMap map, ResourceData type, int newValue);
}
