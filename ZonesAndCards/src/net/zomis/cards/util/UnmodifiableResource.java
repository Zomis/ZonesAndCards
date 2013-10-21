package net.zomis.cards.util;

public class UnmodifiableResource implements IResource {

	private final String	name;
	private final int	mDefault;

	UnmodifiableResource() { this(null, 0); }
	public UnmodifiableResource(String name, int defaultValue) {
		this.name = name;
		this.mDefault = defaultValue;
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
