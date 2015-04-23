package net.zomis.cards.greger;

import net.zomis.cards.model.CardModel;

public class GregerCardModel extends CardModel {

	public GregerCardModel(GregerSuite gs, int i) {
		super(gs.toString() + "-" + i);
	}

	public GregerCardModel(GregerSuite gs, GregerEffect stop) {
		super(String.valueOf(gs) + stop.toString());
	}

}
