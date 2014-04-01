package net.zomis.cards.cwars2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceMap;

public class CWars2Card extends CardModel {

	CWars2Card() { this(null); }
	public CWars2Card(String name) {
		super(name);
		this.extras = new LinkedList<PublicAction>();
	}

	final ResourceMap costs = new ResourceMap();
	final ResourceMap effects = new ResourceMap();
	final ResourceMap opponentEffects = new ResourceMap();
	
	private final List<PublicAction> extras;
	
	public ResourceMap getCosts() {
		return new ResourceMap(costs, true);
	}
	public ResourceMap getEffects() {
		return new ResourceMap(effects, true);
	}
	public ResourceMap getOpponentEffects() {
		return new ResourceMap(opponentEffects, true);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getName());
		for (Entry<IResource, Integer> type : costs.getValues()) {
			if (type.getValue() != null)
				str.append("\n" + type.getValue() + " " + type.getKey());
		}
		for (Entry<IResource, Integer> type : effects.getValues()) {
			if (type.getValue() != null)
				str.append("\nMe " + type.getValue() + " " + type.getKey());
		}
		for (Entry<IResource, Integer> type : opponentEffects.getValues()) {
			if (type.getValue() != null)
				str.append("\nOpp " + type.getValue() + " " + type.getKey());
		}
//		if (damage != 0)
//			str.append("\nAttack " + getDamage());
//		if (castleDamage != 0)
//			str.append("\nAttack Castle " + getCastleDamage());
		return str.toString();
	}

	void addAction(PublicAction action) {
		this.extras.add(action);
	}

	public boolean checkAllowed() {
		for (PublicAction act : extras) {
			if (!act.actionIsAllowed())
				return false;
		}
		return true;
	}
	void perform(CWars2Game game) {
		for (PublicAction act : extras) {
			act.onPerform(); // for replay reasons, do not use game Stack here
//			game.addAndProcessStackAction(act);
		}
	}
	public int castleDamage() {
		return -this.opponentEffects.getResources(CWars2Res.CASTLE);
	}
	public int damage() {
		return -this.opponentEffects.getResources(CWars2Res.WALL);
	}
}
