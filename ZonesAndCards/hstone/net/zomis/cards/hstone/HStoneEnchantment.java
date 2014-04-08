package net.zomis.cards.hstone;

import net.zomis.cards.model.CardZone;

public class HStoneEnchantment {
	
	private HStoneCard	source;
	private int	attack;
	private int	health;

	public HStoneEnchantment(HStoneCard source, int attack, int health) {
		this.source = source;
		this.attack = attack;
		this.health = health;
	}
	
	public boolean isActive() {
		CardZone<?> zone = this.source.getCurrentZone();
		if (zone == null)
			return false;
		return zone == source.getPlayer().getBattlefield() || zone == source.getPlayer().getNextPlayer().getBattlefield();
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getHealth() {
		return health;
	}
	
	public HStoneCard getSource() {
		return source;
	}
	

}
