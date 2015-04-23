package net.zomis.cards.wart;

@FunctionalInterface
public interface HSChangeCount {
	int determineCount(HStoneCard source, HStoneCard target, int input);
}
