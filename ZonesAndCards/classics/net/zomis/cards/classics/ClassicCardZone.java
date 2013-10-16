package net.zomis.cards.classics;

import java.util.Map.Entry;
import java.util.SortedMap;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

public class ClassicCardZone extends CardZone {

	public ClassicCardZone(String zoneName) {
		super(zoneName);
	}
	public void addDeck(int wildcards) {
		for (Entry<Suite, SortedMap<Integer, ClassicCard>> ee : this.getGame().getCardModels().entrySet()) {
			if (ee.getKey().isWildcard()) {
				this.addWildcards(wildcards);
			}
			else {
				addSuite(ee.getKey());
			}
		}
	}
	public void addSuites(Suite... suites) {
		for (Suite suite : suites) {
			this.addSuite(suite);
		}
	}
	private void addSuite(Suite suite) {
		for (Entry<Integer, ClassicCard> inner : getGame().getCardModels().get(suite).entrySet()) {
			this.createCardOnBottom(inner.getValue());
		}
	}
	public void addWildcards(int count) {
		SortedMap<Integer, ClassicCard> wildcards = this.getGame().getCardModels().get(Suite.EXTRA);
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
	public Card getTopCard() {
		return this.cardList().peekFirst();
	}
	
}
