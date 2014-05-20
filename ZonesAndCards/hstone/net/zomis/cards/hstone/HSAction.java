package net.zomis.cards.hstone;

@FunctionalInterface
public interface HSAction {
	void performEffect(HStoneCard source, HStoneCard target);
}
