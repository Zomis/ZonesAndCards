package net.zomis.cards.simple;

import net.zomis.cards.model.Card;
import net.zomis.cards.simple.cardeffects.PublicStackAction;

public interface SimpleStackActionFactory {

	PublicStackAction createAction(SimpleCard cardModel, Card card, SimplePlayer byPlayer);
	
}
