package net.zomis.cards.components;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.DeckPlayer;
import net.zomis.custommap.view.ZomisLog;

public class DeckSourceComponent implements Component, DeckPlayer<CompCardModel> {
	
	private final List<CompCardModel> deckSource;
	
	public DeckSourceComponent(CompPlayer player) {
		this.deckSource = new ArrayList<>();
	}
	
	public List<CompCardModel> getDeckSource() {
		return deckSource;
	}

	public void appendTo(CardZone<CardWithComponents> deck) {
		ZomisLog.info("Appending cards: " + deckSource);
		for (CompCardModel model : deckSource) {
			deck.createCardOnBottom(model);
		}
	}

	@Override
	public int getCardCount() {
		return deckSource.size();
	}

	@Override
	public void addCard(CompCardModel card) {
		deckSource.add(card);
	}

}
