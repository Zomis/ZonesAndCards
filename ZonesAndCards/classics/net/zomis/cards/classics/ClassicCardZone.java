package net.zomis.cards.classics;

import java.util.Map.Entry;
import java.util.SortedMap;

import net.zomis.cards.model.CardZone;

public class ClassicCardZone extends CardZone {

	public ClassicCardZone(String zoneName) {
		super(zoneName);
	}
	public void addDeck(ClassicGame game, int wildcards) {
		for (Entry<Suite, SortedMap<Integer, ClassicCard>> ee : game.getCardModels().entrySet()) {
			if (ee.getKey().isWildcard()) {
				this.addWildcards(game, wildcards);
			}
			else {
				addSuite(game, ee.getKey());
			}
		}
	}
	public void addSuites(ClassicGame game, Suite... suites) {
		for (Suite suite : suites) {
			this.addSuite(game, suite);
		}
	}
	private void addSuite(ClassicGame game, Suite suite) {
		for (Entry<Integer, ClassicCard> inner : game.getCardModels().get(suite).entrySet()) {
			this.createCardOnBottom(inner.getValue());
		}
	}
	public void addWildcards(ClassicGame game, int count) {
		SortedMap<Integer, ClassicCard> wildcards = game.getCardModels().get(Suite.EXTRA);
		if (wildcards == null)
			throw new IllegalStateException("No wildcard models found");
		for (int i = 0; i < count; i++) {
			for (ClassicCard card : wildcards.values()) {
				this.createCardOnBottom(card);
			}
		}
	}
	
	@Override
	public ClassicGame getGame() {
		return (ClassicGame) super.getGame();
	}
	
}
