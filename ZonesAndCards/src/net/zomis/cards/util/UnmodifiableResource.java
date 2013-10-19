package net.zomis.cards.util;

public class UnmodifiableResource implements IResource {

	private final String	name;
	private final int	min;
	private final int	max;
	private final int	mDefault;

	public UnmodifiableResource(String name, int min, int max, int defaultValue) {
		this.name = name;
		this.min = min;
		this.max = max;
		this.mDefault = defaultValue;
	}

	@Override
	public int getMax() {
		return this.max;
	}

	@Override
	public int getMin() {
		return this.min;
	}

	@Override
	public int getDefault() {
		return this.mDefault;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
