package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.zomis.cards.hstone.ench.HStoneEnchSilence;
import net.zomis.cards.hstone.events.HStoneDamagedEvent;
import net.zomis.cards.hstone.events.HStoneHealEvent;
import net.zomis.cards.hstone.events.HStoneMinionDiesEvent;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.triggers.HStoneTrigger;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.events.IEvent;

public class HStoneCard extends Card<HStoneCardModel> {
	
	private final ResourceMap res;
	private final EnumSet<HSAbility> abilities;
	private final List<HStoneTrigger<?>> triggers;
	
	public HStoneCard(HStoneCardModel model, CardZone<?> initialZone) {
		super(model);
		this.currentZone = initialZone;
		this.res = new ResourceMap();
		this.res.set(HStoneRes.ATTACK, model.getAttack());
		this.res.set(HStoneRes.HEALTH, model.getHealth());
		this.abilities = EnumSet.noneOf(HSAbility.class);
		abilities.addAll(model.getAbilities());
		if (abilities.contains(HSAbility.CHARGE))
			this.res.set(HStoneRes.ACTION_POINTS, 1);
		
		this.triggers = new ArrayList<HStoneTrigger<?>>(model.getTriggers());
		
		for (HStoneTrigger<?> trigger : triggers) {
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

	void onEndTurn() {
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
	
	List<IEvent> cleanup() {
		List<IEvent> result = new ArrayList<IEvent>();
		int damage = res.getResources(HStoneRes.AWAITING_DAMAGE);
		int heal = res.getResources(HStoneRes.AWAITING_HEAL);
		int healthChange = heal - damage;
		
		if (healthChange < 0 && this.hasAbility(HSAbility.DIVINE_SHIELD)) {
			this.removeAbility(HSAbility.DIVINE_SHIELD);
			healthChange = 0;
		}
		
		int hpChange = healthChange;
		
		if (healthChange < 0 && res.hasResources(HStoneRes.ARMOR, 1)) {
			int armorPain = Math.min(-healthChange, res.getResources(HStoneRes.ARMOR));

			res.changeResources(HStoneRes.ARMOR, -armorPain);
			hpChange = healthChange + armorPain;
			res.changeResources(HStoneRes.HEALTH, hpChange);
		}
		else res.changeResources(HStoneRes.HEALTH, healthChange);
		
		if (hpChange > 0) {
			result.add(new HStoneHealEvent(this));
		}
		else if (hpChange < 0) {
			result.add(new HStoneDamagedEvent(this));
		}
		
		res.set(HStoneRes.AWAITING_DAMAGE, 0);
		res.set(HStoneRes.AWAITING_HEAL, 0);
		
		if (isType(CardType.MINION) || isType(CardType.PLAYER)) {
			if (getHealth() < 1) {
				result.add(new HStoneMinionDiesEvent(this));
				getGame().addStackAction(new StackAction() {
					protected void onPerform() {
						destroy();
					}
				});
			}
		}
		return result;
	}

	public void removeAbility(HSAbility ability) {
		this.abilities.remove(ability);
	}

	public boolean isMinion() {
		return this.getModel().isMinion();
	}

	public void silence() {
		this.getGame().addEnchantment(new HStoneEnchSilence(this));
	}

	public void destroy() {
		this.silence();
		this.getResources().set(HStoneRes.HEALTH, 0);
		this.zoneMoveOnTop(null);
	}

	public boolean isType(CardType type) {
		return getModel().isType(type);
	}

	@Override
	public String getDescription() {
		StringBuilder str = new StringBuilder();
		str.append("(" + getModel().getManaCost() + ") ");
		str.append(getModel().getName());
		if (this.isMinion()) {
			str.append(" " + getAttack() + "/" + getHealth() + "/" + getHealthMax());
			str.append(" " + getModel().getAbilities());
		}
		return str.toString();
	}

	public boolean isAlive() {
		return getHealth() >= 1 
				&& getCurrentZone() != null; // use || or && ?? Technically, it shouldn't matter.
	}

	public int getHealth() {
		return getResources().getResources(HStoneRes.HEALTH);
	}

	public int getHealthMax() {
		return getGame().getResources(this, HStoneRes.MAX_HEALTH);
	}

	public int getAttack() {
		return getGame().getResources(this, HStoneRes.ATTACK);
	}

	public boolean hasTrigger(HStoneTrigger<?> trigger) {
		return this.triggers.contains(trigger);
	}
	
}
