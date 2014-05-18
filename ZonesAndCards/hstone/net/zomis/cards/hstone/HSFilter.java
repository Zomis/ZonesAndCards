package net.zomis.cards.hstone;

@FunctionalInterface
public interface HSFilter {

	boolean shouldKeep(HStoneCard searcher, HStoneCard target);

	default HSFilter and(final HSFilter other) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
				return HSFilter.this.shouldKeep(searcher, target) && other.shouldKeep(searcher, target);
			}
		};
	}

}
