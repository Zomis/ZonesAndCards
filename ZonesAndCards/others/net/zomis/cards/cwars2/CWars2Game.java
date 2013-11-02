package net.zomis.cards.cwars2;

import java.util.ArrayList;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.FixedResourceStrategy;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceMap;
import net.zomis.events.Event;

public class CWars2Game extends CardGame {

	private static final int NUM_PLAYERS	= 2;
	public static final int	MIN_CARDS_IN_DECK_DEFAULT	= 75;
	private int discarded = 0;
	private final int discardsPerTurn = 3;

	private int minCardsInDeck = MIN_CARDS_IN_DECK_DEFAULT;
	
	private CardZone discard;
	private boolean discardMode;
	private List<IResource> knownResources = new ArrayList<IResource>();
	
	public List<IResource> getKnownResources() {
		return knownResources;
	}
	
	public CWars2Game() {
		this.setActionHandler(new CWars2Handler());
		
		for (Producers prod : CWars2Res.Producers.values()) {
			knownResources.add(prod);
			knownResources.add(prod.getResource());
		}
		knownResources.add(CWars2Res.CASTLE);
		knownResources.add(CWars2Res.WALL);
		
		new CWars2CardSet().addCards(this);
		for (int i = 0; i < NUM_PLAYERS; i++) {
			CWars2Player player = new CWars2Player("Player" + i);
			this.addPlayer(player);
			this.addPhase(new CWars2Phase(player));
			for (IResource res : knownResources)
				player.getResources().changeResources(res, 0);
			addZone(player.getDeck());
			addZone(player.getHand());
		}
		discard = new CardZone("DiscardPile");
		discard.setGloballyKnown(true);
		addZone(discard);
		discard.createCardOnTop(new CardModel("NULL"));
	}

	@Event
	public void onAfterAction(AfterActionEvent event) {
//		if (event.getGame() != this) // never happens
//			throw new AssertionError();
//		if (this.isGameOver())
//			return;//			throw new IllegalStateException("Game is already finished."); // always happens
		
		for (Player pl : this.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			int castle = player.getResources().getResources(CWars2Res.CASTLE);
			if (castle <= 0 || castle >= 100)
				this.endGame();
		}
	}
	
	@Event
	public void onPhaseChange(PhaseChangeEvent event) {
		this.fillHands();
		this.discarded = 0;
		this.discardMode = false;
	}
	
	public CWars2Game addDefaultDecks() {
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
			if (player.getCardCount() < getMinCardsInDeck())
				throw new IllegalStateException("Not enough cards added for " + pl + ". Use addDefaultDecks() to add the default decks.");
			player.saveDeck();
			player.fillHand();
		}
		
		ResourceMap res = this.getPlayers().get(0).getResources();
		for (Producers prod : Producers.values()) {
			res.setResourceStrategy(prod, new FixedResourceStrategy(0));
		}
//		new CWars2CardSet.MultiplyNextResourceIncome(this, 0, false);
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
	public CardZone getDiscard() {
		return discard;
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
		return this.discarded > 0 || getCurrentPlayer().getHand().size() < getCurrentPlayer().getHandSize(); 
	}

	public int getDiscardsPerTurn() {
		return this.discardsPerTurn;
	}

	public int getMinCardsInDeck() {
		return this.minCardsInDeck;
	}

}
