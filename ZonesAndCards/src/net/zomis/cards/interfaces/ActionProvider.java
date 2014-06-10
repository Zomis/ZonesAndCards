package net.zomis.cards.interfaces;

import net.zomis.cards.model.StackAction;

@FunctionalInterface
public interface ActionProvider {
	StackAction get();
}
