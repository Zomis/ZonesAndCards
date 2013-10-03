package net.zomis.cards.cwars2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;

public class CWars2Card extends CardModel {

	public CWars2Card(String name) {
		super(name);
		this.extras = new LinkedList<>();
	}

	ResourceMap costs = new ResourceMap();
	ResourceMap effects = new ResourceMap();
	ResourceMap opponentEffects = new ResourceMap();
	
	int	damage;
	int	castleDamage;
	private final List<StackAction> extras;
	
	public ResourceMap getCosts() {
		return costs;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getName());
		for (Entry<ResourceType, Integer> type : costs.getValues()) {
			if (type.getValue() != null)
				str.append("\n" + type.getValue() + " " + type.getKey());
		}
		for (Entry<ResourceType, Integer> type : effects.getValues()) {
			if (type.getValue() != null)
				str.append("\nMe " + type.getValue() + " " + type.getKey());
		}
		for (Entry<ResourceType, Integer> type : opponentEffects.getValues()) {
			if (type.getValue() != null)
				str.append("\nOpp " + type.getValue() + " " + type.getKey());
		}
		if (damage != 0)
			str.append("\nAttack " + damage);
		if (castleDamage != 0)
			str.append("\nAttack Castle " + castleDamage);
		return str.toString();
	}

	void addAction(StackAction action) {
		this.extras.add(action);
	}

	public boolean isAllowed() {
		for (StackAction act : extras) {
			if (!act.isAllowed())
				return false;
		}
		return true;
	}
	void perform(CWars2Game game) {
		for (StackAction act : extras) {
			game.addAndProcessStackAction(act);
		}
	}
}
