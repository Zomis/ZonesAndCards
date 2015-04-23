package net.zomis.cards.resources.common;

import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.resources.ResourceStrategy;

public class FixedResourceStrategy implements ResourceStrategy {

	private final int	value;

	FixedResourceStrategy() { this(0); }
	public FixedResourceStrategy(int value) {
		this.value = value;
	}
	
	@Override
	public int getResourceAmount(ResourceData type, ResourceMap map) {
		return value;
	}

}
