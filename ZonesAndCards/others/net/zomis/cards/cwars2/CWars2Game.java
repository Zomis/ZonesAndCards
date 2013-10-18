package net.zomis.cards.cwars2;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.events.AfterActionEvent;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.util.ResourceType;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.Event;

public class CWars2Game extends CardGame {

	private static final int	NUM_PLAYERS	= 2;
	public static final int	MIN_CARDS_IN_DECK	= 15;
	private int discarded = 0;
	private final int discardsPerTurn = 3;
	
	ResourceType bricks;
	ResourceType weapons;
	ResourceType crystals;
	ResourceType builders;
	ResourceType recruits;
	ResourceType wizards;
	ResourceType castle;
	ResourceType wall;
	ResourceType[] restypes;
	ResourceType[] producers;
	private CardZone	discard;
	private boolean discardMode;
	
	public CWars2Game() {
		this.setAIHandler(new CWars2Handler());
		this.bricks = new ResourceType("Bricks");
		this.builders = new ResourceType("Builders");
		this.weapons = new ResourceType("Weapons");
		this.recruits = new ResourceType("Recruits");
		this.crystals = new ResourceType("Crystals");
		this.wizards = new ResourceType("Mage");
		this.castle = new ResourceType("Castle");
		this.wall = new ResourceType("Wall");
		this.restypes = new ResourceType[]{ bricks, weapons, crystals };
		this.producers = new ResourceType[]{ builders, recruits, wizards };
//		this.cards = new ArrayList<CWars2Card>();
		
		new CWars2CardSet().addCards(this);
		
//		for (CWars2Card card : this.cards) {
//			this.addCard(card);
//		}
		
		for (int i = 0; i < NUM_PLAYERS; i++) {
			CWars2Player player = new CWars2Player("Player" + i);
			this.addPlayer(player);
			this.addPhase(new CWars2Phase(player));
			
			for (ResourceType res : restypes) {
				player.getResources().set(res, 8);
			}
			for (ResourceType res : producers) {
				player.getResources().set(res, 2);
			}
			player.getResources().set(castle, 25);
			player.getResources().set(wall, 15);
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
			int castle = player.getResources().getResources(this.castle);
			if (castle <= 0)
				this.endGame();
			if (castle >= 100)
				this.endGame();
		}
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
	
//	public List<CWars2Card> getCards() {
//		return Collections.unmodifiableList(cards);
//	}
	public ResourceType[] getRestypes() {
		return restypes;
	}
	public ResourceType[] getProducers() {
		return producers;
	}
	@Override
	public boolean nextPhase() {
		if (!this.isNextPhaseAllowed())
			return false;
		
		boolean sup = super.nextPhase();
		// TODO: listen for PhaseChangeEvent
		this.fillHands();
		this.discarded = 0;
		this.discardMode = false;

		return sup;
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

	public void discarded() {
		this.discarded++;
	}

	public ResourceType getResWall() {
		return this.wall;
	}
	public ResourceType getResCastle() {
		return castle;
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
		return getCurrentPlayer().getHand().size() < getCurrentPlayer().getHandSize(); 
	}

	public int getDiscardsPerTurn() {
		return this.discardsPerTurn;
	}
}
