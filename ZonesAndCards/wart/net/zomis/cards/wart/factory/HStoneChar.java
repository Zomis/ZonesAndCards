package net.zomis.cards.wart.factory;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.util.DeckList;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneClass;

public class HStoneChar {

	private final String name;
	private final HStoneClass charClass;
	private final DeckList deck;
	private final HStoneCardModel charModel;

	public HStoneChar(String name, HStoneClass charClass, DeckList deck) {
		this.name = name;
		this.charClass = charClass;
		this.deck = deck;
		this.charModel = new HStoneCardModel("PLAYER", 0, CardType.PLAYER);
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
