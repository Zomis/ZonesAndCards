package net.zomis.cards.wart;

@FunctionalInterface
public interface HSGetCount {
	int determineCount(HStoneCard source, HStoneCard target);
}
