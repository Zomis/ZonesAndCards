package net.zomis.cards.cwars2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.DeckPlayer;


public class CWars2Player extends Player implements DeckPlayer<CWars2Card> {

	private final CardZone deck;
	private final CardZone hand;
	private final int handSize = 8;
	
	private List<CWars2Card> cards;
	
	CWars2Player() { this(null); }
	public CWars2Player(String name) {
		this.setName(name);
		this.deck = new CardZone("Deck-" + getName());
		this.hand = new CardZone("Hand-" + getName());
		this.hand.setKnown(this, true);
		this.cards = new LinkedList<CWars2Card>();
		
	}
	public CardZone getDeck() {
		return deck;
	}
	public CardZone getHand() {
		return hand;
	}
	
	public int getHandSize() {
		return handSize;
	}
	void fillHand() {
		while (this.getHand().cardList().size() < this.handSize) {
			drawCard();
		}
	}
	
	private void drawCard() {
		if (deck.cardList().isEmpty()) {
			if (this.cards.isEmpty())
				throw new IllegalStateException("Player has no cards: " + this);
			
			for (CWars2Card card : this.cards) {
				deck.createCardOnTop(card);
			}
			deck.shuffle();
		}
		
		deck.getTopCard().zoneMoveOnBottom(this.hand);
	}
	@Override
	public CWars2Game getGame() {
		return (CWars2Game) super.getGame();
	}
	@Override
	public void addCard(CWars2Card field) {
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
		return super.toString() + "--" + this.getHand() + " Resources " + this.getResources();
	}
	public List<CWars2Card> getCards() {
		return Collections.unmodifiableList(this.cards);
	}
	public CWars2Player getNextPlayer() {
		return (CWars2Player) this.getOpponents().get(0);
	}
	
}
