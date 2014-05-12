package net.zomis.cards.crgame;

public interface CRFilter {
	public boolean test(CRCard source, CRCard target);

	public default CRFilter and(CRFilter other) {
		return (src, dest) -> test(src, dest) && other.test(src, dest);
	}
	
}
