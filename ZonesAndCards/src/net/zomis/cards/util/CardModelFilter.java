package net.zomis.cards.util;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.utils.ZomisList.FilterInterface;

public class CardModelFilter implements FilterInterface<Card<?>> {
	private final CardModel	model;
	
	public CardModelFilter(CardModel model) {
		this.model = model;
	}
	
	@Override
	public boolean shouldKeep(Card<?> obj) {
		return obj != null && model.equals(obj.getModel());
	}
}
