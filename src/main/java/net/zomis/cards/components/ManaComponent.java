package net.zomis.cards.components;

import net.zomis.cards.iface.Component;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;


public class ManaComponent implements Component {

	private enum ManaRes implements IResource {
		MANA, MAX_MANA;

		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(resource);
		}
	}

	private final ResourceMap map;
	
	public ManaComponent(ResourceMap map, int maxHealth) {
		this.map = map;
		map.set(ManaRes.MANA, maxHealth);
		map.set(ManaRes.MAX_MANA, maxHealth);
	}

	public void use(int mana) {
		map.changeResources(ManaRes.MANA, -mana);
	}
	
	public boolean has(int mana) {
		return map.hasResources(ManaRes.MANA, mana);
	}
	
	public void restoreAllMana() {
		map.set(ManaRes.MANA, getMaxMana());
	}
	
	public int getMana() {
		return map.get(ManaRes.MANA);
	}
	
	public int getMaxMana() {
		return map.get(ManaRes.MAX_MANA);
	}
	
	public void setMaxMana(int value) {
		map.set(ManaRes.MAX_MANA, value);
	}
	
	@Override
	public String toString() {
		return "Mana " + getMana() + " / " + getMaxMana();
	}
	
}
