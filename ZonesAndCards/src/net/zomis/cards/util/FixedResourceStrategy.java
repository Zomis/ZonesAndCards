package net.zomis.cards.util;

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
