package net.zomis.cards.simple.cardeffects;

import net.zomis.cards.model.Card;
import net.zomis.cards.simple.SimpleCard;
import net.zomis.cards.simple.SimplePlayer;

public interface SimpleStackAction {

	void perform(SimpleCard cardModel, Card card, SimplePlayer byPlayer);

}
