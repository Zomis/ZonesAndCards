package net.zomis.cards.components;


public class HealthComponent implements PlayerComponent {

	private int	maxHealth;
	private int	health;

	public HealthComponent(int maxHealth) {
		this.maxHealth = maxHealth;
		this.health = maxHealth;
	}

	public boolean isAlive() {
		return health > 0;
	}
	
	public boolean isDamaged() {
		return health >= maxHealth;
	}
	
	public void damage(int damage) {
		this.health -= damage;
	}
	
	public void heal(int heal) {
		this.health += heal;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public String toString() {
		return "Health " + health + " / " + maxHealth;
	}
	
}
