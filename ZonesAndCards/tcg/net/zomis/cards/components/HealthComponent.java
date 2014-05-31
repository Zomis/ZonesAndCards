package net.zomis.cards.components;

import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;


public class HealthComponent implements PlayerComponent {

	private enum HealthRes implements IResource {
		HEALTH, MAX_HEALTH;

		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(resource);
		}
	}

	private final ResourceMap map;
	
	public HealthComponent(ResourceMap map, int maxHealth) {
		this.map = map;
		map.set(HealthRes.MAX_HEALTH, maxHealth);
		map.set(HealthRes.HEALTH, maxHealth);
	}

	public boolean isAlive() {
		return map.get(HealthRes.HEALTH) > 0;
	}
	
	public boolean isDamaged() {
		return getHealth() >= getMaxHealth();
	}
	
	public void damage(int damage) {
		map.changeResources(HealthRes.HEALTH, -damage);
	}
	
	public void heal(int heal) {
		if (heal + getHealth() >= getMaxHealth())
			map.set(HealthRes.HEALTH, getMaxHealth());
		else map.changeResources(HealthRes.HEALTH, heal);
	}
	
	public int getHealth() {
		return map.get(HealthRes.HEALTH);
	}
	
	public int getMaxHealth() {
		return map.get(HealthRes.MAX_HEALTH);
	}
	
	@Override
	public String toString() {
		return "Health " + getHealth() + " / " + getMaxHealth();
	}
	
}
