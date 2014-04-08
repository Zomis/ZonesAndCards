package net.zomis.cards.hstone;


public interface HSFilter {

	boolean shouldKeep(HStoneCard searcher, HStoneCard target);

}
