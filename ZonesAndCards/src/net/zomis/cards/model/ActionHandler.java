package net.zomis.cards.model;

import java.util.List;


public interface ActionHandler {
	// TODO: Add StackAction(s) for dragAndDrop(Card source, Card destination)
	// TODO: Add boolean or StackAction for startDrag(Card source)
	
//	List<StackAction> getAvailableActions(CardGame<Player, CardModel> cardGame, Player player);
	StackAction click(Card<?> card);
	<E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(E cardGame, Player player);
}
