package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.zomis.cards.hstone.ench.HStoneEnchForward;
import net.zomis.cards.hstone.ench.HStoneEnchSilence;
import net.zomis.cards.hstone.ench.HStoneEnchantment;
import net.zomis.cards.hstone.events.HStoneDamagedEvent;
import net.zomis.cards.hstone.events.HStoneHealEvent;
import net.zomis.cards.hstone.events.HStoneMinionDiesEvent;
import net.zomis.cards.hstone.factory.CardType;
import net.zomis.cards.hstone.factory.HSAbility;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.hstone.triggers.HStoneTrigger;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.events.IEvent;

public class HStoneCard extends Card<HStoneCardModel> {
	
	private final ResourceMap res = new ResourceMap();
	private final EnumSet<HSAbility> abilities = EnumSet.noneOf(HSAbility.class);
	private final List<HStoneTrigger<?>> triggers;
	private final List<HStoneEnchantment> enchantmentResponsibles = new ArrayList<>();
	
	public HStoneCard(HStoneCardModel model, CardZone<?> initialZone) {
		super(model);
		currentZone = initialZone;
		res.set(HStoneRes.ATTACK, model.getAttack());
		res.set(HStoneRes.HEALTH, model.getHealth());
		res.set(HStoneRes.SPELL_DAMAGE, model.getSpellDamage());
		
		abilities.addAll(model.getAbilities());
		if (abilities.contains(HSAbility.CHARGE))
			this.res.set(HStoneRes.ACTION_POINTS, 1);
		
		this.triggers = new ArrayList<HStoneTrigger<?>>(model.getTriggers());
		
		for (HStoneTrigger<?> trigger : triggers) {
			this.getGame().registerHandler(trigger.getClazz(), trigger.createForCard(this));
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
				&& !hasAbility(HSAbility.FROZEN) && !hasAbility(HSAbility.NO_ATTACK);
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

	public HStonePlayer getPlayer() {
		CardZone<HStoneCard> zone = getCurrentZone();
		if (zone == null)
			throw new NullPointerException("Zone is null for " + this);
		Player owner = zone.getOwner();
		return (HStonePlayer) owner;
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
		this.abilities.clear();
		this.triggers.clear();
		this.getGame().addEnchantment(new HStoneEnchSilence(this));
	}

	public void destroy() {
		this.silence();
		this.getResources().set(HStoneRes.HEALTH, 0);
		this.zoneMoveOnBottom(getPlayer().getDiscard());
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
				&& currentZone != null && currentZone != getPlayer().getDiscard(); // use || or && ?? Technically, it shouldn't matter.
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
	
	public HStoneCard getLeftAdjacent() {
		LinkedList<HStoneCard> list = getCurrentZone().cardList();
		int index = list.indexOf(this);
		if (index == 0)
			return null;
		return list.get(index - 1);
	}
	
	public HStoneCard getRightAdjacent() {
		LinkedList<HStoneCard> list = getCurrentZone().cardList();
		int index = list.indexOf(this);
		if (index == list.size() - 1)
			return null;
		return list.get(index + 1);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CardZone<HStoneCard> getCurrentZone() {
		return (CardZone<HStoneCard>) super.getCurrentZone();
	}
	
	public HStoneCard copyTo(CardZone<HStoneCard> toZone) {
		final HStoneCard copy = toZone.createCardOnBottom(getModel());
		for (Entry<IResource, ResourceData> ee : getResources().getData().entrySet())
			copy.getResources().set(ee.getKey(), ee.getValue().getRealValue());
		copy.abilities.clear();
		copy.abilities.addAll(abilities);
		copy.triggers.clear();
		copy.triggers.addAll(triggers); // TODO: Create copies of the triggers.
		
		for (HStoneEnchantment ench : getGame().getEnchantments()) {
			if (ench.appliesTo(this) && !ench.appliesTo(copy)) {
				getGame().addEnchantmentAfter(new HStoneEnchForward(ench) {
					@Override
					public boolean appliesTo(HStoneCard card) {
						return card == copy;
					}
				}, ench);
			}
		}
		
		return copy;
	}

	public void addTrigger(HStoneTrigger<?> trigger) {
		this.triggers.add(trigger);
		this.getGame().registerHandler(trigger.getClazz(), trigger.createForCard(this));
	}

	public boolean isPlayer() {
		return this == getPlayer().getPlayerCard();
	}

	public int getManaCost() {
		return getModel().getManaCost();
//		return getResources().getResources(HStoneRes.MANA_COST);
	}

	public void addEnchantment(HStoneEnchantment enchantment) {
		this.enchantmentResponsibles.add(enchantment);
	}
	
}
