package net.zomis.cards.model;

import java.util.List;


public interface ActionHandler {
	// TODO: Add StackAction(s) for dragAndDrop(Card source, Card destination)
	// TODO: Add boolean or StackAction for startDrag(Card source)
	StackAction click(Card card);
	List<StackAction> getAvailableActions(CardGame cardGame, Player player);
}
