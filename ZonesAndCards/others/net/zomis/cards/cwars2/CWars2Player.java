package net.zomis.cards.cwars2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.HandPlayer;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.DeckPlayer;


public class CWars2Player extends Player implements DeckPlayer<CWars2Card>, HandPlayer {

	private final CardZone<Card<CWars2Card>> deck;
	private final CardZone<Card<CWars2Card>> hand;
	
	private List<CWars2Card> cards;
	private CardZone<Card<CWars2Card>> discard;
	
	CWars2Player() {
		this(null);
	}
	
	public CWars2Player(String name) {
		this.setName(name);
		this.deck = new CardZone<Card<CWars2Card>>("Deck", this);
		this.hand = new CardZone<Card<CWars2Card>>("Hand", this);
		this.hand.setKnown(this, true);
		this.cards = new ArrayList<CWars2Card>();
		this.discard = new CardZone<Card<CWars2Card>>("Discard", this);
		this.discard.setGloballyKnown(true);
	}
	
	@Override
	public CardZone<Card<CWars2Card>> getDeck() {
		return deck;
	}
	
	public CardZone<Card<CWars2Card>> getHand() {
		return hand;
	}
	
	public int handSize() {
		return this.getResources().getResources(CWars2Res.HANDSIZE);
	}
	
	void fillHand() {
		while (this.getHand().size() < this.handSize()) {
			drawCard();
		}
	}
	
	private void drawCard() {
		if (deck.isEmpty()) {
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
	@Override
	public CWars2Player getNextPlayer() {
		return (CWars2Player) super.getNextPlayer();
	}
	@Override
	public void clearCards() {
		this.cards = new ArrayList<CWars2Card>();
		// Need to create new list here because it can be unmodifiable already.
	}
	public CardZone<Card<CWars2Card>> getDiscard() {
		return this.discard;
	}
	
}
