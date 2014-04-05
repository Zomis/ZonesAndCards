package net.zomis.cards.cwars2;

import java.util.ArrayList;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.cards.resources.common.FixedResourceStrategy;
import net.zomis.events.EventHandlerGWT;

public class CWars2Game extends CardGame<CWars2Player, CWars2Card> {
	private static final int NUM_PLAYERS = 2;
	public static final int	MIN_CARDS_IN_DECK = 75;
	
	private int discarded = 0;
	private final int discardsPerTurn = 3;

	private int minCardsInDeck = MIN_CARDS_IN_DECK;
	
	private boolean discardMode;
	private List<IResource> knownResources = new ArrayList<IResource>();
	
	public List<IResource> getKnownResources() {
		return knownResources;
	}
	
	CWars2Game() {
		this.setActionHandler(new CWars2Handler());
		
		// Certain resources MUST exist. Wall is for damage and Castle is for castle damage and the target of the game.
		for (CWars2Res res : CWars2Res.values())
			this.addResource(res);
		for (int i = 0; i < NUM_PLAYERS; i++) {
			CWars2Player player = new CWars2Player("Player" + i);
			this.addPlayer(player);
			this.addPhase(new CWars2Phase(player));
			addZone(player.getDeck());
			addZone(player.getHand());
			addZone(player.getDiscard());
		}
		this.registerHandler(AfterActionEvent.class, new EventHandlerGWT<AfterActionEvent>() {
			@Override
			public void executeEvent(AfterActionEvent event) {
				onAfterAction(event);
			}
		});
		this.registerHandler(PhaseChangeEvent.class, new EventHandlerGWT<PhaseChangeEvent>() {
			@Override
			public void executeEvent(PhaseChangeEvent event) {
				onPhaseChange(event);
			}
		});
	}
	
	public void addResource(IResource res) {
		knownResources.add(res);
	}
	
	void init() {
		for (Player pl : this.getPlayers()) {
			for (IResource res : knownResources)
				pl.getResources().changeResources(res, 0);
		}
	}
	

	private void onAfterAction(AfterActionEvent event) {
//		if (this.isGameOver())
//			return;//			throw new IllegalStateException("Game is already finished."); // always happens
//		ZomisLog.info(event.getAction());
		for (Player pl : this.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			int castle = player.getResources().getResources(CWars2Res.CASTLE);
			if (!this.isGameOver()) {
				if (castle <= 0 || castle >= 100)
					this.endGame();
			}
		}
	}
	
	private void onPhaseChange(PhaseChangeEvent event) {
		this.fillHands();
		this.discarded = 0;
		this.discardMode = false;
	}
	
	@Deprecated
	CWars2Game addRandomDecks() {
		for (Player pl : this.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			CWars2DeckBuilder deckBuilder = new CWars2DeckBuilder(new ScoreConfigFactory<CWars2Player, CWars2Card>());
			deckBuilder.createDeck(player, getMinCardsInDeck());
		}
		return this;
	}
	
	@Override
	protected void onStart() {
		for (Player pl : this.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			int minCards = getMinCardsInDeck();
			int playerCount = player.getCardCount();
			if (playerCount < minCards)
				throw new IllegalStateException("Not enough cards added for " + pl + ". Use addDefaultDecks() to add the default decks. Expected " + minCards + " but found " + playerCount);
			player.saveDeck();
			player.fillHand();
		}
		
		ResourceMap res = this.getPlayers().get(0).getResources();
		for (Producers prod : Producers.values()) {
			// This is for preventing first player from getting resources on first turn.
			res.setResourceStrategy(prod, new FixedResourceStrategy(0));
		}
		this.setActivePhaseDirectly(this.getPhases().get(0));
	}
	
	private void fillHands() {
		for (Player pl : getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			player.fillHand();
		}
	}

	public int getDiscarded() {
		return discarded;
	}

	void discarded() {
		this.discarded++;
	}
	@Override
	public CWars2Player getCurrentPlayer() {
		return (CWars2Player) super.getCurrentPlayer();
	}
	
	public void toggleDiscardMode() {
		this.discardMode = !this.discardMode;
	}
	public boolean isDiscardMode() {
		return discardMode;
	}
	
	@Override
	public boolean isNextPhaseAllowed() {
		// this.discarded > 0 ||  // discarded is also increased directly before nextphase is called when you play
		return this.discarded > 0 || getCurrentPlayer().getHand().size() < getCurrentPlayer().handSize(); 
	}

	public int getDiscardsPerTurn(CWars2Player player) {
		return this.discardsPerTurn;
	}

	public int getMinCardsInDeck() {
		return this.minCardsInDeck;
	}

	public CWars2Player determineWinner() {
		int winner = -1;
		for (int i = 0; i < getPlayers().size(); i++) {
			ResourceMap res = getPlayers().get(i).getResources();
			Boolean winStatus = winStatus(res);
			if (winStatus != null) {
				int newWin = -1;
				if (winStatus == false)
					newWin = getPlayers().size() - 1 - i; // winner is the other player
				else newWin = i;
				
				if (winner >= 0 && winner != newWin)
					throw new IllegalStateException("Two winners");
				
				winner = newWin;
			}
		}
		if (winner < 0)
			return null;
		return (CWars2Player) getPlayers().get(winner);
	}
	private Boolean winStatus(ResourceMap res) {
		if (res.getResources(CWars2Res.CASTLE) <= 0)
			return false;
		if (res.getResources(CWars2Res.CASTLE) >= 100)
			return true;
		return null;
	}
}
