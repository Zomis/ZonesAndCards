package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;

public class DamageDealtEvent extends CompCard2Event {

	private int damage;

	public DamageDealtEvent(CardWithComponents source, CardWithComponents target, int damage) {
		super(source, target);
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
	}

}
