package net.zomis.cards.model;

import java.util.List;


public interface AIHandler {
	StackAction click(Card card);
	List<StackAction> getAvailableActions(Player player);
}
