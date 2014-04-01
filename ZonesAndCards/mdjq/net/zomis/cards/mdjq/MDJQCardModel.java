package net.zomis.cards.mdjq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQRes.TribalType;
import net.zomis.cards.mdjq.activated.ActivatedAbility;
import net.zomis.cards.mdjq.cards.TriggeredAbility;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.iterate.CastedIterator;

public class MDJQCardModel extends CardModel {

	public MDJQCardModel(String name) {
		super(name);
	}

	private ResourceMap manaCost = new ResourceMap();
	private Collection<CardType>	types;
	private Collection<ActivatedAbility> activated = new LinkedHashSet<ActivatedAbility>();
	private Collection<TriggeredAbility> triggered = new LinkedHashSet<TriggeredAbility>();
	private Collection<TribalType> extraTypes = new LinkedList<MDJQRes.TribalType>();
	private int	power;
//	private Collection<StaticAbility> statics = new HashSet<StaticAbility>();
	private int	toughness;
	
	public int getPower() {
		return power;
	}
	public int getToughness() {
		return toughness;
	}

	public int getCardLimit() {
		if (getTypes().contains(CardType.BASIC))
			return Integer.MAX_VALUE;
		else return 4;
	}
	
	public Collection<CardType> getTypes() {
		if (this.types == null)
			throw new NullPointerException("Types not initialized for card " + this.getName());
		return Collections.unmodifiableCollection(types);
	}
	public Collection<TribalType> getExtraTypes() {
		return Collections.unmodifiableCollection(extraTypes);
	}
	
	public MDJQCardModel addTrigger(TriggeredAbility triggered) {
		this.triggered.add(triggered);
		return this;
	}
	
	public Collection<TriggeredAbility> getTriggeredAbilities() {
		return new ArrayList<TriggeredAbility>(triggered);
	}
	
	public MDJQCardModel addCost(MColor type, int cost) {
		this.manaCost.set(type, cost);
		return this;
	}
	public MDJQCardModel setTypes(CardType... types) {
		this.types = Arrays.asList(types);
		return this;
	}

	public MDJQCardModel addActivatedAbility(ActivatedAbility ability) {
		this.activated.add(ability);
		return this;
	}
	public MDJQCardModel setCTypes(TribalType... types) {
		this.extraTypes = Arrays.asList(types);
		return this;
	}
	public MDJQCardModel setPT(int power, int toughness) {
		this.power = power;
		this.toughness = toughness;
		return this;
	}
	
	public MDJQPermanent createCard(CardZone initialZone) {
		for (MDJQPlayer player : new CastedIterator<Player, MDJQPlayer>(initialZone.getGame().getPlayers())) {
			if (player.getLibrary() == initialZone)
				return new MDJQPermanent(this, initialZone, player); 
		}
		throw new IllegalArgumentException("Zone " + initialZone + " is not any player's library. Need to know who is the owner of the card.");
	}
	public ResourceMap getManaCost() {
		return manaCost;
	}

	public Collection<ActivatedAbility> getActivatedAbilities() {
		return this.activated;
	}

	public boolean isType(CardType type) {
		return this.types.contains(type);
	}

	public boolean isPermanent() {
		for (CardType type : this.types)
			if (type.isPermanent())
				return true;
		return false;
	}
}
