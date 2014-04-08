package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneTrigger;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.resources.ResourceMap;

public class HStoneCard extends Card<HStoneCardModel> {
	
	private final ResourceMap res;
	private final EnumSet<HSAbility> abilities;
	private final List<HStoneTrigger> triggers;
	private final List<HStoneEnchantment> enchantments;

	public HStoneCard(HStoneCardModel model, CardZone<?> initialZone) {
		super(model);
		this.currentZone = initialZone;
		this.res = new ResourceMap();
		this.res.set(HStoneRes.ATTACK, model.getAttack());
		this.res.set(HStoneRes.HEALTH, model.getHealth());
		this.abilities = EnumSet.noneOf(HSAbility.class);
		this.enchantments = new ArrayList<HStoneEnchantment>();
		abilities.addAll(model.getAbilities());
		if (abilities.contains(HSAbility.CHARGE))
			this.res.set(HStoneRes.ACTION_POINTS, 1);
		
		this.triggers = new ArrayList<HStoneTrigger>(model.getTriggers());
		
		for (HStoneTrigger trigger : triggers) {
			this.getGame().registerHandler(trigger.getClazz(), trigger.forCard(this));
		}
	}

	@Override
	public HStoneCardModel getModel() {
		return (HStoneCardModel) super.getModel();
	}
	
	@Override
	public HStoneGame getGame() {
		return (HStoneGame) super.getGame();
	}
	
	public ResourceMap getResources() {
		return res;
	}

	public boolean isAttackPossible() {
		boolean hasAttack = this.getModel().isType(CardType.POWER) || getResources().hasResources(HStoneRes.ATTACK, 1);
		return hasAttack && getResources().hasResources(HStoneRes.ACTION_POINTS, 1)
				&& !hasAbility(HSAbility.FROZEN);
	}

	public void onEndTurn() {
		if (!hasAbility(HSAbility.FROZEN)) {
			int actions = hasAbility(HSAbility.WINDFURY) ? 2 : 1;
			res.set(HStoneRes.ACTION_POINTS, actions);
		}
		abilities.remove(HSAbility.FROZEN);
	}

	public boolean hasAbility(HSAbility ability) {
		return abilities.contains(ability);
	}

	@Deprecated
	public boolean isFrozen() {
		return hasAbility(HSAbility.FROZEN);
	}

	public void addAbility(HSAbility abilityn) {
		abilities.add(abilityn);
	}

	public void damage(int damage) {
		FightModule.damage(this, damage);
	}

	public void heal(int healing) {
		FightModule.heal(this, healing);
	}

	public HStonePlayer getPlayer() {
		return (HStonePlayer) getCurrentZone().getOwner();
	}

	public void cleanup() {
		ResourceMap res = getResources();
		int damage = res.getResources(HStoneRes.AWAITING_DAMAGE);
		int heal = res.getResources(HStoneRes.AWAITING_HEAL);
		
		res.changeResources(HStoneRes.HEALTH, heal - damage);
		res.set(HStoneRes.AWAITING_DAMAGE, 0);
		res.set(HStoneRes.AWAITING_HEAL, 0);
		
		if (!res.hasResources(HStoneRes.HEALTH, 1))
			this.destroy();
	}

	public void removeAbility(HSAbility ability) {
		this.abilities.remove(ability);
	}

	public boolean isMinion() {
		return this.getModel().isMinion();
	}

	public void enchant(HStoneEnchantment enchantment) {
		this.enchantments.add(enchantment);
	}

	public int getAttackBonus() {
		int i = 0;
		for (HStoneEnchantment ench : enchantments)
			i += ench.getAttack();
		return i;
	}

	public int getHealthBonus() {
		int i = 0;
		for (HStoneEnchantment ench : enchantments)
			i += ench.getHealth();
		return i;
	}
	
	public void silence() {
	}

	public void destroy() {
		this.zoneMoveOnTop(null);
	}

	public boolean isType(CardType type) {
		return getModel().isType(type);
	}

}
