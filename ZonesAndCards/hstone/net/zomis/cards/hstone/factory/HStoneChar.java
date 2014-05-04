package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.DeckList;

public class HStoneChar {

	private final String name;
	private final HStoneClass charClass;
	private final DeckList deck;
	private final HStoneCardModel charModel;

	public HStoneChar(String name, HStoneClass charClass, DeckList deck) {
		this.name = name;
		this.charClass = charClass;
		this.deck = deck;
		this.charModel = new HStoneCardModel("PLAYER", -1, CardType.PLAYER);
		this.charModel.setPT(0, 30);
	}
	
	public String getName() {
		return name;
	}
	
	public DeckList getDeck() {
		return deck;
	}
	
	public HStoneClass getCharClass() {
		return charClass;
	}

	public HStoneCard playerCard(CardZone<HStoneCard> specialZone) {
		return specialZone.createCardOnTop(charModel);
	}
	
}
