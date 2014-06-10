package net.zomis.cards.crgame;

import java.util.List;

import net.zomis.cards.count.CardCount;
import net.zomis.cards.interfaces.HandPlayer;
import net.zomis.cards.interfaces.HasBattlefield;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckList;
import net.zomis.cards.util.DeckPlayer;
import net.zomis.utils.ZomisList;

public class CRPlayer extends Player implements DeckPlayer<CRCardModel>, HandPlayer, HasBattlefield {

//	private static final int	MAX_HAND_SIZE	= 7;
	private static final int	BASE_HOURS = 2;
	private static final int	START_QUALITY = 50;
	
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
		
		this.hand.setKnown(this, true);
		this.battlefield.setGloballyKnown(true);
		this.getResources().set(CRRes.HOURS_AVAILABLE, 0);
		this.getResources().set(CRRes.QUALITY, START_QUALITY);
	}
	
	@Override
	public int getCardCount() {
		return deck.size();
	}

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
	public CardZone<CRCard> getBattlefield() {
		return battlefield;
	}

	@Override
	public CardZone<CRCard> getHand() {
		return hand;
	}

	public CRCard drawCard() {
//		if (this.hand.size() == MAX_HAND_SIZE)
//			return null;
		
		CRCard card = this.deck.getTopCard();
		if (card == null) {
			List<CardCount<CRCardModel>> cards = this.getCards().getCount(getGame());
			ZomisList.filter(cards, cm -> !this.hasCard(cm));
			DeckBuilder.createExact(this, cards);
			deck.shuffle();
			card = deck.getTopCard();
		}
		card.zoneMoveOnBottom(hand);
		return card;
	}

	private boolean hasCard(CardCount<CRCardModel> cardCount) {
		for (CRCard card : getBattlefield()) {
			if (card.getModel() == cardCount.getModel())
				return true;
		}
		for (CRCard card : getHand()) {
			if (card.getModel() == cardCount.getModel())
				return true;
		}
		return false;
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
		getResources().changeResources(CRRes.HOURS_AVAILABLE, BASE_HOURS);
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

	public int getTotalCards() {
		return this.hand.size() + this.deck.size();
	}
	
}
