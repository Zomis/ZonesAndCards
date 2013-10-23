package net.zomis.cards.turneight;

import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.model.CastedIterator;

public class TurnEightGame extends ClassicGame {
	private static final int NUM_CARDS = 6;
	private static final int DRAW_MAX = 3;
	private final ClassicCardZone deck;
	private final ClassicCardZone discard;
	
	private Suite currentSuite;
	private Suite playerChoice = Suite.SPADES;
	
	public void setPlayerChoice(Suite playerChoice) {
		this.playerChoice = playerChoice;
	}
	public Suite getPlayerChoice() {
		return playerChoice;
	}
	private boolean hasPlayed;
	CardModel drawCard;
	CardModel nextTurn;
	private int	drawnCards = 0;
	public int getDrawnCards() {
		return drawnCards;
	}
	
	public void setHasPlayed(ClassicCard card) {
		if (card.getRank() != this.getAceValue())
			this.hasPlayed = true;
	}
	public boolean hasPlayed() {
		return hasPlayed;
	}
	
	/*
	 * "Vändåtta"
	 * http://sv.wikipedia.org/wiki/V%C3%A4nd%C3%A5tta
	 * Var sin tur, lägg ett eller många kort, om man inte kan får man dra kort -- max 3
	 * 
	 * 8or vänder "färg"
	 * Ess gör att alla andra måste dra ett kort
	 * */
	public TurnEightGame() {
		super(AceValue.HIGH);
		this.deck = new ClassicCardZone("Deck");
		this.discard = new ClassicCardZone("Pile");
		this.discard.setGloballyKnown(true);
		this.addZone(deck);
		this.addZone(discard);
		this.deck.addDeck(this, 0);
		
		this.drawCard = new CardModel("Draw card");
		this.nextTurn = new CardModel("Next turn");
		
		CardZone actions = this.addZone(new CardZone("Actions"));
		actions.createCardOnBottom(drawCard);
		actions.createCardOnBottom(nextTurn);
		actions.setGloballyKnown(true);
		
		CardZone colorPickZone = this.addZone(new CardZone("ChosenSuite"));
		for (Suite suite : Suite.values()) {
			if (!suite.isWildcard())
				colorPickZone.createCardOnBottom(new SuiteModel(suite));
		}
		colorPickZone.setGloballyKnown(true);
	}
	
	@Override
	public void onStart() {
		this.deck.shuffle();
		for (int i = 0; i < NUM_CARDS; i++) {
			for (Player player : new CastedIterator<Player, CardPlayer>(this.getPlayers())) {
				deck.getTopCard().zoneMoveOnBottom(((CardPlayer) player).getHand());
			}
		}
		
		ClassicCard firstCardModel = null;
		do {
			if (firstCardModel != null) {
				CustomFacade.getLog().i("First card was " + firstCardModel + ", reshuffling.");
				discard.moveToBottomOf(deck);
				deck.shuffle();
			}
			Card firstCard = deck.cardList().peek();
			firstCard.zoneMoveOnTop(this.discard);
			firstCardModel = (ClassicCard) firstCard.getModel();
			setCurrentSuite(firstCardModel.getSuite());
		}
		while (TurnEightController.isSpecial(firstCardModel, this.getAceValue()));
	}
	
	public Suite getCurrentSuite() {
		return currentSuite;
	}
	public void setCurrentSuite(Suite currentSuite) {
		this.currentSuite = currentSuite;
	}
	public CardZone getDeck() {
		return deck;
	}
	public CardZone getDiscard() {
		return discard;
	}
	
	@Override
	public ActionHandler getActionHandler() {
		return new TurnEightController();
	}
	
	public TurnEightGame addPlayer(String name, CardAI ai) {
		this.addPlayer(name);
		this.getPlayers().get(this.getPlayers().size() - 1).setAI(ai);
		return this;
	}
	public TurnEightGame addPlayer(String name) {
		final CardPlayer player = new CardPlayer();
		player.setName(name);
		player.setHand(new ClassicCardZone("Hand-" + name));
		player.getHand().setKnown(player, true);
		this.addPlayer(player);
		this.addZone(player.getHand());
		this.addPhase(new PlayerPhase(player));
		return this;
	}
	
	@Override
	public boolean nextPhase() {
		boolean result = false;
		if (isNextPhaseAllowed()) {
			result = super.nextPhase();
			this.hasPlayed = false;
			this.drawnCards = 0;
		}
		else CustomFacade.getLog().w("Player has not made a move.");
		
		if (this.getCurrentPlayer().getHand().isEmpty()) {
			for (Player pl : this.getPlayers()) {
				CardPlayer player = (CardPlayer) pl;
				if (!player.getHand().cardList().isEmpty()) { // if any player exist who still have something on their hand
					this.nextPhase(); // call recursively
					return result;
				}
			}
			this.endGame();
		}
		return result;
	}
	
	@Override
	public boolean isNextPhaseAllowed() {
		return hasPlayed() || this.drawnCards == DRAW_MAX || getCurrentPlayer().getHand().cardList().isEmpty();
	}
	public void playerForceDraw(CardPlayer player) {
		if (player == null)
			throw new NullPointerException("Null player cannot draw a card.");
		if (this.deck.cardList().isEmpty()) {
			Card last = this.discard.cardList().getLast();
			this.discard.moveToBottomOf(this.deck);
			last.zoneMoveOnTop(this.discard);
			this.deck.shuffle();
		}
		if (this.deck.getTopCard() == null) {
			throw new AssertionError("How on earth can top card be null? Players: " + this.getPlayers() + " Discard pile is " + this.discard.cardList());
		}
		this.deck.getTopCard().zoneMoveOnBottom(player.getHand());
	}
	public boolean playerDrawFromDeck(CardPlayer player) {
		if (this.drawnCards == DRAW_MAX) {
			return false;
		}
		this.playerForceDraw(player);
		this.drawnCards++;
		return true;
	}
	
}
