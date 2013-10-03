package net.zomis.cards.simple;

import net.zomis.cards.model.CardModel;

public class SimpleCard extends CardModel {

	private int	cost;
	private SimpleStackActionFactory action;

	public SimpleCard(String name) {
		super(name);
	}

	public SimpleStackActionFactory getAction() {
		return action;
	}
	public SimpleCard setAction(SimpleStackActionFactory action) {
		this.action = action;
		return this;
	}
	public SimpleCard setCost(int i) {
		this.cost = i;
		return this;
	}
	
	public int getCost() {
		return cost;
	}
	@Override
	public String toString() {
		return cost + "--" + super.toString();
	}
	
}
