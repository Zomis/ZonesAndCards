package net.zomis.cards.hstone.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.hstone.triggers.HStoneTrigger;
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
	private HStoneMinionType minionTypes;
	private HStoneEffect	effect;
	private final List<HStoneTrigger<?>> triggers;
	private HStoneClass forClazz;
	int overload;
	int spellDamage;
	
	public int getManaCost() {
		return manaCost;
	}

	public void forClass(HStoneClass clazz) {
		forClazz = clazz;
	}
	
	public HStoneCardModel(String name, int manaCost, CardType type) {
		super(name);
		this.triggers = new ArrayList<HStoneTrigger<?>>();
		this.manaCost = manaCost;
		this.type = type;
		this.abilities = new HashSet<HSAbility>();
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
	
	public boolean isWeapon() {
		return type == CardType.WEAPON;
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

	public void addTriggerEffect(HStoneTrigger<?> trigger) {
		this.triggers.add(trigger);
	}
	
	public List<HStoneTrigger<?>> getTriggers() {
		return new ArrayList<HStoneTrigger<?>>(this.triggers);
	}

	public boolean isOfType(HStoneMinionType minionType) {
		return minionTypes == minionType;
	}
	
	public HStoneMinionType getMinionType() {
		return minionTypes;
	}

	public void addType(HStoneMinionType type) {
		minionTypes = type;
	}

	public boolean isType(CardType type) {
		return this.type == type;
	}

	public HStoneClass getForClazz() {
		return forClazz;
	}

	public int getOverload() {
		return overload;
	}
	
	public int getSpellDamage() {
		return spellDamage;
	}

	public boolean isSecret() {
		return false;
	}
}
