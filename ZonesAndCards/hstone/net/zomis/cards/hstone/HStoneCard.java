package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.factory.HStoneTrigger;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.resources.ResourceMap;

public class HStoneCard extends Card<HStoneCardModel> implements HStoneTarget {
	
	private final ResourceMap res;
	private final EnumSet<HSAbility> abilities;
	private final List<HStoneTrigger> triggers;

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
	
	@Override
	public ResourceMap getResources() {
		return res;
	}

	@Override
	public boolean isAttackPossible() {
		return getResources().hasResources(HStoneRes.ATTACK, 1) && getResources().hasResources(HStoneRes.ACTION_POINTS, 1)
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

	@Override
	public boolean isFrozen() {
		return hasAbility(HSAbility.FROZEN);
	}

	@Override
	public void addAbility(HSAbility abilityn) {
		abilities.add(abilityn);
	}

	@Override
	public void damage(int damage) {
		FightModule.damage(this, damage);
	}

	@Override
	public void heal(int healing) {
		FightModule.heal(this, healing);
	}

	@Override
	public HStonePlayer getPlayer() {
		return (HStonePlayer) getCurrentZone().getOwner();
	}

	public void cleanup() {
		if (res.getResources(HStoneRes.HEALTH) <= 0)
			this.zoneMoveOnTop(null);
	}

}
