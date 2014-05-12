package net.zomis.cards.crgame;


@FunctionalInterface
public interface CREffect {
	void apply(CRCard source, CRCard target);
}
