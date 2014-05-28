package net.zomis.cards.components;


public class HealthComponent implements PlayerComponent {

	private int	maxHealth;
	private int	health;

	public HealthComponent(int maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}
	
}
