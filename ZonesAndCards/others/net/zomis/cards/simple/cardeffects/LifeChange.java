package net.zomis.cards.simple.cardeffects;

import net.zomis.cards.model.Card;
import net.zomis.cards.simple.SimpleCard;
import net.zomis.cards.simple.SimplePlayer;

public class LifeChange implements SimpleStackAction {

	private final int value;
	
	public LifeChange(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public void perform(SimpleCard cardModel, Card card, SimplePlayer byPlayer) {
		byPlayer.changeLife(value);
	}

}
