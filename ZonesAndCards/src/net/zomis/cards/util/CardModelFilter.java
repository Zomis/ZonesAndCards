package net.zomis.cards.util;

import net.zomis.ZomisList.FilterInterface;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;

public class CardModelFilter implements FilterInterface<Card> {
	private CardModel	model;
	public CardModelFilter(CardModel model) {
		this.model = model;
	}
	@Override
	public boolean shouldKeep(Card obj) {
		return obj != null && model.equals(obj.getModel());
	}
}
