package net.zomis.cards.util;

public class ResourceType {

	private String	name;

	public ResourceType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
}
