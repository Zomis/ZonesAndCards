package net.zomis.cards.model;

import java.util.List;


public interface ActionHandler {
	StackAction click(Card<?> card);
	
	List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player);
}
