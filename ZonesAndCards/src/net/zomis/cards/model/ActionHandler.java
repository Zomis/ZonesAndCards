package net.zomis.cards.model;

import java.util.List;


public interface ActionHandler {
	// TODO: Add StackAction(s) for dragAndDrop(Card source, Card destination)
	// TODO: Add boolean or StackAction for startDrag(Card source)
	
	StackAction click(Card<?> card);
	
	@Deprecated
	<E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(E cardGame, Player player);
	
	List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player);
}
