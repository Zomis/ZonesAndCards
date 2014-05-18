package net.zomis.cards.model;

@FunctionalInterface
public interface ActionProvider {
	StackAction get();
}
