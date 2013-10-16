package net.zomis.cards.cwars2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.util.ResourceType;


public class CWars2Game extends CardGame {

	private static final int	NUM_PLAYERS	= 2;
	private int discarded = 0;
	private final int discardsPerTurn = 3;
	
	private List<CWars2Card> cards;
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
		this.cards = new ArrayList<CWars2Card>();
		
		new CWars2CardSet().addCards(this);
		
		for (CWars2Card card : this.cards) {
			this.addCard(card);
		}
		
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
			
			CWars2DeckBuilder deckBuilder = new CWars2DeckBuilder(new ScoreConfigFactory<CWars2Player, CWars2Card>());
			deckBuilder.createDeck(player, 15);
			player.saveDeck();
			player.fillHand();
		}
		discard = new CardZone("DiscardPile");
		discard.setGloballyKnown(true);
		discard.createCardOnTop(new CardModel("NULL"));
		addZone(discard);
	}

	
	@Override
	public AIHandler getAIHandler() {
		return new CWars2Handler();
	}
	public List<CWars2Card> getCards() {
		return Collections.unmodifiableList(cards);
	}
	public ResourceType[] getRestypes() {
		return restypes;
	}
	public ResourceType[] getProducers() {
		return producers;
	}
	@Override
	public void setActivePhase(GamePhase phase) {
		this.discarded = 0;
		this.discardMode = false;
		this.getCurrentPlayer().fillHand();
		super.setActivePhase(phase);
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
		return this.discarded > 0; // discarded is also increased directly before nextphase is called when you play
	}

	public int getDiscardsPerTurn() {
		return this.discardsPerTurn;
	}
}
