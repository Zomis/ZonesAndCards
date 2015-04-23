package net.zomis.cards.wart;

@FunctionalInterface
public interface HSAction {
	void performEffect(HStoneCard source, HStoneCard target);
}
