package net.zomis.cards.simple.cardeffects;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.simple.SimpleCard;
import net.zomis.cards.simple.SimplePlayer;

public class OpponentLifeChange extends LifeChange implements SimpleStackAction {

	public OpponentLifeChange(int value) {
		super(value);
	}

	@Override
	public void perform(SimpleCard cardModel, Card card, SimplePlayer byPlayer) {
		for (Player pl : byPlayer.getOpponents()) {
			SimplePlayer pl2 = (SimplePlayer) pl;
			pl2.changeLife(getValue());
		}
	}
	
}
