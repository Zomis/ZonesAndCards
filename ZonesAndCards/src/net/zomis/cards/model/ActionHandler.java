package net.zomis.cards.model;

import java.util.List;


public interface ActionHandler {
	StackAction click(Card card);
	List<StackAction> getAvailableActions(Player player);
}
