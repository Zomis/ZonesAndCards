package net.zomis.cards.hstone.ench;

import net.zomis.cards.hstone.HStoneCard;

public class HStoneEnchSpecificPT extends HStoneEnchPT {
	private final HStoneCard	target;
	private final int	attack;
	private final int	health;

	public HStoneEnchSpecificPT(HStoneCard target, int attack, int health) {
		this.target = target;
		this.attack = attack;
		this.health = health;
	}
	
	@Override
	public boolean appliesTo(HStoneCard card) {
		return target == card;
	}

	@Override
	protected Integer attack(HStoneCard card, Integer value) {
		return attack + value;
	}

	@Override
	protected Integer health(HStoneCard card, Integer value) {
		return health + value;
	}

}
