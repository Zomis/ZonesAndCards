package net.zomis.cards.crgame;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.HandPlayer;
import net.zomis.cards.model.HasBattlefield;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.DeckList;
import net.zomis.cards.util.DeckPlayer;

public class CRPlayer extends Player implements DeckPlayer<CRCardModel>, HandPlayer, HasBattlefield {

	private static final int	MAX_HAND_SIZE	= 7;
	private static final int	BASE_HOURS = 2;
	private static final int	START_QUALITY = 100;
	
	private final CardZone<CRCard> deck;
	private final CardZone<CRCard> hand;
	private final CardZone<CRCard> battlefield;
	private final CardZone<CRCard> discard;
	private final DeckList deckList;
	
	public CRPlayer(String name, DeckList deckList) {
		super();
		this.setName(name);
		this.deck = new CardZone<CRCard>("Deck", this);
		this.hand = new CardZone<CRCard>("Hand", this);
		this.battlefield = new CardZone<CRCard>("Battlefield", this);
		this.discard = new CardZone<CRCard>("Discard", this);
		this.deckList = deckList;
		this.getResources().set(CRRes.QUALITY, START_QUALITY);
	}
	
	@Override
	public int getCardCount() {
		return deck.size();
	}

	@Override
	public CardZone<CRCard> getDeck() {
		return deck;
	}

	public CardZone<CRCard> getDiscard() {
		return discard;
	}
	
	@Override
	public void addCard(CRCardModel card) {
		deck.createCardOnBottom(card);
	}

	@Override
	public void clearCards() {
		deck.cardList().clear();
	}

	@Override
	public CardZone<CRCard> getBattlefield() {
		return battlefield;
	}

	@Override
	public CardZone<CRCard> getHand() {
		return hand;
	}

	public CRCard drawCard() {
		if (this.hand.size() == MAX_HAND_SIZE)
			return null;
		
		CRCard card = this.deck.getTopCard();
		if (card == null)
			return null;
		card.zoneMoveOnBottom(hand);
		return card;
	}

	public void drawCards(int startCards) {
		for (int i = 0; i < startCards; i++)
			drawCard();
	}

	public DeckList getCards() {
		return this.deckList;
	}
	
	void newTurn() {
		drawCard();
		getResources().set(CRRes.HOURS_AVAILABLE, BASE_HOURS);
		for (CRCard card : this.battlefield) {
			card.newTurn();
		}
	}
	
	@Override
	public CRCardGame getGame() {
		return (CRCardGame) super.getGame();
	}

	public CardZone<CRCard> getZombieZone() {
		return this.battlefield;
	}

	public CardZone<CRCard> getUserZone() {
		return this.battlefield;
	}

	public int getQuality() {
		return getResources().getResources(CRRes.QUALITY);
	}
	
	@Override
	public CRPlayer getNextPlayer() {
		return (CRPlayer) super.getNextPlayer();
	}
	
}
