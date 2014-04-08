package net.zomis.cards.hstone.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;

public class HStoneCardModel extends CardModel {

	private final CardType	type;
	private final int manaCost;
	private int attack;
	private int health;
	private HStoneRarity rarity;
	private final Set<HSAbility> abilities;
	private final EnumSet<HStoneMinionType> minionTypes;
	private HStoneEffect	effect;
	private final List<HStoneTrigger> triggers;
	
	public int getManaCost() {
		return manaCost;
	}

	public HStoneCardModel(String name, int manaCost, CardType type) {
		super(name);
		this.triggers = new ArrayList<HStoneTrigger>();
		this.manaCost = manaCost;
		this.type = type;
		this.abilities = new HashSet<HSAbility>();
		this.minionTypes = EnumSet.noneOf(HStoneMinionType.class);
	}
	
	void setRarity(HStoneRarity rarity) {
		this.rarity = rarity;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <E extends CardModel> Card<E> createCardInternal(CardZone<?> initialZone) {
		Card<HStoneCardModel> card = new HStoneCard(this, initialZone);
		return (Card<E>) card;
	}

	void setPT(int attack, int health) {
		this.attack = attack;
		this.health = health;
	}
	
	void addAbility(HSAbility ability) {
		this.abilities.add(ability);
	}

	public boolean isSpell() {
		return type == CardType.SPELL;
	}
	
	public boolean isMinion() {
		return type == CardType.MINION;
	}
	
	public boolean isSecret() {
		return type == CardType.SECRET;
	}
	
	public int getAttack() {
		return attack;
	}
	public int getHealth() {
		return health;
	}
	
	
	public HStoneRarity getRarity() {
		return rarity;
	}

	public Collection<? extends HSAbility> getAbilities() {
		return this.abilities;
	}

	void setEffect(HStoneEffect effect) {
		this.effect = effect;
	}

	public HStoneEffect getEffect() {
		return this.effect;
	}

	public HSFilter getTargetFilter() {
		if (effect == null)
			return null;
		return effect.needsTarget() ? effect : null;
	}

	public void addTriggerEffect(HStoneTrigger battlecryTrigger) {
		this.triggers.add(battlecryTrigger);
	}
	
	public List<HStoneTrigger> getTriggers() {
		return new ArrayList<HStoneTrigger>(this.triggers);
	}

	public boolean isOfType(HStoneMinionType minionType) {
		return minionTypes.contains(minionType);
	}

	public void addType(HStoneMinionType type) {
		minionTypes.add(type);
	}

	public boolean isType(CardType type) {
		return this.type == type;
	}
	
}
