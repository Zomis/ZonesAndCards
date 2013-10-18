package net.zomis.cards.cwars2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.DeckPlayer;
import net.zomis.cards.util.ResourceMap;


public class CWars2Player extends Player implements DeckPlayer<CWars2Card> {

	private final CardZone library;
	private final CardZone hand;
	private final int handSize = 8;
	
	private ResourceMap resources = new ResourceMap();
	private List<CWars2Card> cards;
	
	public CWars2Player(String name) {
		this.setName(name);
		this.library = new CardZone("Deck-" + getName());
		this.hand = new CardZone("Hand-" + getName());
		this.hand.setKnown(this, true);
		this.cards = new LinkedList<CWars2Card>();
		
	}
	public ResourceMap getResources() {
		return resources;
	}
	public CardZone getDeck() {
		return library;
	}
	public CardZone getHand() {
		return hand;
	}
	
	public int getHandSize() {
		return handSize;
	}
	public void fillHand() {
		while (this.getHand().cardList().size() < this.handSize) {
//			CustomFacade.getLog().i(this + " Hand size is " + this.getHand().cardList().size());
			drawCard();
		}
	}
	
	private void drawCard() {
		if (library.cardList().isEmpty()) {
			if (this.cards.isEmpty())
				throw new IllegalStateException("Player has no cards: " + this);
			
			for (CWars2Card card : this.cards) {
				library.createCardOnTop(card);
			}
			library.shuffle();
		}
		
		library.getTopCard().zoneMoveOnBottom(this.hand);
	}
	@Override
	public CWars2Game getGame() {
		return (CWars2Game) super.getGame();
	}
	public void addCard(CWars2Card field) {
//		CustomFacade.getLog().i(this + " Add card " + field);
		this.cards.add(field);
	}
	public void saveDeck() {
		Collections.sort(this.cards);
		this.cards = Collections.unmodifiableList(this.cards);
	}
	
	public int getCardCount() {
		return this.cards.size();
	}
	@Override
	public String toString() {
		return super.toString() + "--" + this.getHand().cardList().toString() + " Resources " + this.resources;
//		StringBuilder str = new StringBuilder();
//		for (CWars2Card card : this.cards) {
//			if (str.length() > 0) str.append(",");
//			str.append(card.getName());
//		}
//		return super.toString() + "--" + this.resources.toString() + "---" + str.toString();
	}
	public List<CWars2Card> getCards() {
		return Collections.unmodifiableList(this.cards);
	}
	
}
