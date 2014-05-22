package net.zomis.cards.hstone;

@FunctionalInterface
public interface HSGetCount {
	int determineCount(HStoneCard source, HStoneCard target);
}
