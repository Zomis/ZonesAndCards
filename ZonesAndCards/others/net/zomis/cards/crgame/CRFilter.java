package net.zomis.cards.crgame;

@FunctionalInterface
public interface CRFilter {
	boolean test(CRCard source, CRCard target);

	default CRFilter and(CRFilter other) {
		return new CRFilter() {
			@Override
			public boolean test(CRCard src, CRCard dest) {
				// Can't use a lambda here because of GWT
				return CRFilter.this.test(src, dest) && other.test(src, dest);
			}
		};
	}
	
}
