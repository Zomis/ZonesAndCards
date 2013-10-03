package net.zomis.cards.turneight;

import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.custommap.CustomFacade;

public class TurnEightGame extends ClassicGame {
	private static final int NUM_CARDS = 6;
	private static final int DRAW_MAX = 3;
	private final ClassicCardZone deck;
	private final ClassicCardZone discard;
	
	private Suite currentSuite;
	private Suite playerChoice = Suite.SPADES;
	
	public void setPlayerChoice(Suite playerChoice) {
		CustomFacade.getLog().i("Set Player suite: " + playerChoice);
//		new Exception().printStackTrace();
		this.playerChoice = playerChoice;
	}
	public Suite getPlayerChoice() {
		return playerChoice;
	}
	private boolean hasPlayed;
	private CardModel	drawCard;
	private CardModel	nextTurn;
	private int	drawnCards = 0;
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
	 * ### 2or som sänker och ess som vänder bort osv. är i "**** och President"
	 * 
	 * 8or vänder "färg"
	 * Ess gör att alla andra måste dra ett kort
	 * */
	public TurnEightGame(String[] players) {
		super(AceValue.HIGH);
		this.setRandomSeed(42);
		this.deck = new ClassicCardZone("Deck");
		this.discard = new ClassicCardZone("Pile");
		this.discard.setGloballyKnown(true);
		this.addZone(deck);
		this.addZone(discard);
		this.deck.addDeck(0);
		deck.shuffle();
		
		for (final String name : players) {
			final CardPlayer player = new CardPlayer();
			player.setName(name);
			player.setHand(new ClassicCardZone("Hand-" + name));
			player.getHand().setKnown(player, true);
			this.addPlayer(player);
			this.addZone(player.getHand());
			
//			player.getHand().setGloballyKnown(true);
			
			this.addPhase(new PlayerPhase(player));
			for (int i = 0; i < NUM_CARDS; i++)
				deck.getTopCard().zoneMove(player.getHand(), null);
		}
		
		this.drawCard = new CardModel("Draw card");
		this.nextTurn = new CardModel("Next turn");
		
		CardZone actions = this.addZone(new CardZone("Actions"));
		actions.add(drawCard.createCard());
		actions.add(nextTurn.createCard());
		actions.setGloballyKnown(true);
		
		CardZone colorPickZone = this.addZone(new CardZone("ChosenSuite"));
		for (Suite suite : Suite.values()) {
			if (!suite.isWildcard())
				colorPickZone.add(new SuiteModel(suite).createCard());
		}
		colorPickZone.setGloballyKnown(true);
		
		ClassicCard firstCardModel = null;
		do {
			if (firstCardModel != null) {
				CustomFacade.getLog().i("First card was " + firstCardModel + ", reshuffling.");
				for (Card card : this.discard.cardList()) {
					card.zoneMove(deck, null);
				}
				deck.shuffle();
			}
			Card firstCard = deck.cardList().peek();
			firstCard.zoneMove(this.discard, null);
			firstCardModel = (ClassicCard) firstCard.getModel();
			setCurrentSuite(firstCardModel.getSuite());
		}
		while (TurnEightController.isSpecial(firstCardModel, this.getAceValue()));
	}
	
	public Suite getCurrentSuite() {
		return currentSuite;
	}
	public void setCurrentSuite(Suite currentSuite) {
		CustomFacade.getLog().d("Set current suite to " + currentSuite);
		this.currentSuite = currentSuite;
	}
	public CardZone getDeck() {
		return deck;
	}
	public CardZone getDiscard() {
		return discard;
	}
	
	@Override
	public AIHandler getAIHandler() {
		return new TurnEightController();
	}
	
	@Override
	public void setActivePhase(GamePhase phase) {
		if (isNextPhaseAllowed()) {
			this.hasPlayed = false;
			this.drawnCards = 0;
			super.setActivePhase(phase);
		}
		else CustomFacade.getLog().w("Player has not made a move.");
		
		if (this.getCurrentPlayer().getHand().cardList().isEmpty()) {
			for (Player pl : this.getPlayers()) {
				CardPlayer player = (CardPlayer) pl;
				if (!player.getHand().cardList().isEmpty()) { // if any player exist who still have something on their hand
					nextPhase();
					break;
				}
			}
		}
	}
	@Override
	public boolean isNextPhaseAllowed() {
		return hasPlayed() || this.drawnCards == DRAW_MAX || getCurrentPlayer().getHand().cardList().isEmpty();
	}
	public CardModel getDrawCardModel() {
		return this.drawCard;
	}
	public CardModel getNextTurnModel() {
		return this.nextTurn;
	}
	public void playerForceDraw(CardPlayer player) {
		if (this.deck.cardList().isEmpty()) {
			Card last = this.discard.cardList().getLast();
			this.discard.moveAll(this.deck, null);
			last.zoneMove(this.discard, null);
			this.deck.shuffle();
		}
		
		this.deck.cardList().peek().zoneMove(player.getHand(), player);
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
