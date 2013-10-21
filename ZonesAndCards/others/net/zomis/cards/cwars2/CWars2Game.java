package net.zomis.cards.cwars2;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.CWars2Res.Resources;
import net.zomis.cards.events.AfterActionEvent;
import net.zomis.cards.events.PhaseChangeEvent;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.IResource;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.Event;

public class CWars2Game extends CardGame {

	private static final int	NUM_PLAYERS	= 2;
	public static final int	MIN_CARDS_IN_DECK	= 15;
	private int discarded = 0;
	private final int discardsPerTurn = 3;

	private CardZone	discard;
	private boolean discardMode;
	
	public CWars2Game() {
		this.setAIHandler(new CWars2Handler());
		new CWars2CardSet().addCards(this);
		for (int i = 0; i < NUM_PLAYERS; i++) {
			CWars2Player player = new CWars2Player("Player" + i);
			this.addPlayer(player);
			this.addPhase(new CWars2Phase(player));
			
			for (IResource res : Resources.values()) {
				player.getResources().set(res, res.getDefault());
			}
			for (IResource res : Producers.values()) {
				player.getResources().set(res, res.getDefault());
			}
			player.getResources().set(CWars2Res.CASTLE, CWars2Res.CASTLE.getDefault());
			player.getResources().set(CWars2Res.WALL, CWars2Res.WALL.getDefault());
			
			addZone(player.getDeck());
			addZone(player.getHand());
			
			player.getDeck().setGloballyKnown(true);
		}
		discard = new CardZone("DiscardPile");
		discard.setGloballyKnown(true);
		discard.createCardOnTop(new CardModel("NULL"));
		addZone(discard);
	}

	@Event
	public void onAfterAction(AfterActionEvent event) {
		if (event.getAction() instanceof CWars2PlayAction) {
			CWars2PlayAction play = (CWars2PlayAction) event.getAction();
			CustomFacade.getLog().i("Action: " + play.getPlayer() + " played " + play.getCard().getModel().getName());
		}
		else CustomFacade.getLog().i("Action: " + event.getAction());
		for (Player pl : this.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			int castle = player.getResources().getResources(CWars2Res.CASTLE);
			if (castle <= 0)
				this.endGame();
			if (castle >= 100)
				this.endGame();
		}
	}
	
	@Event
	public void onPhaseChange(PhaseChangeEvent event) {
		this.fillHands();
		this.discarded = 0;
		this.discardMode = false;
	}
	
	@Override
	protected void onStart() {
		for (Player pl : this.getPlayers()) {
			CWars2Player player = (CWars2Player) pl;
			CWars2DeckBuilder deckBuilder = new CWars2DeckBuilder(new ScoreConfigFactory<CWars2Player, CWars2Card>());
			deckBuilder.createDeck(player, MIN_CARDS_IN_DECK);
			if (player.getCardCount() < MIN_CARDS_IN_DECK)
				throw new IllegalStateException("Cards not added");
			player.saveDeck();
			player.fillHand();
		}
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

}
