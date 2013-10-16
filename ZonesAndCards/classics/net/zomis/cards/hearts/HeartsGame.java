package net.zomis.cards.hearts;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import net.zomis.ZomisList;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardComparator;
import net.zomis.cards.classics.ClassicCardFilter;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.model.CastedIterator;

public class HeartsGame extends ClassicGame {

	private final ClassicCardZone	pile;
	private final HeartsGiveDirection	giveDirection;
	private final AIHandler handler = new HeartsHandler();
	private boolean heartsBroken;
	
	public HeartsGame(HeartsGiveDirection giveDirection) {
		super(AceValue.HIGH);
		this.giveDirection = giveDirection;
		this.pile = new ClassicCardZone("Current");
		this.pile.setGloballyKnown(true);
		this.addZone(pile);
	}
	
	public boolean isHeartsBroken() {
		return heartsBroken;
	}
	
	public ClassicCardZone getPile() {
		return pile;
	}
	
	public HeartsGiveDirection getGiveDirection() {
		return giveDirection;
	}
	
	public HeartsGame addPlayer(String playerName) {
		CardPlayer player = new CardPlayer();
		player.setName(playerName);
		ClassicCardZone hand = new ClassicCardZone("Hand-" + playerName);
		hand.setKnown(player, true);
		player.setHand(hand);
		this.addZone(hand);
		
		ClassicCardZone board = new ClassicCardZone("GiveAway-" + playerName);
		board.setKnown(player, true);
		player.setBoard(board);
		this.addZone(board);
		
		this.addPhase(new PlayerPhase(player));
		super.addPlayer(player);
		return this;
	}
	
	@Override
	public void onStart() {
		if (this.getPlayers().size() != 4)
			throw new IllegalStateException("Hearts must currently be played with 4 players.");
		
		ClassicCardZone deck = new ClassicCardZone("Deck");
		this.addZone(deck);
		deck.addDeck(0);
		deck.shuffle();
		if (deck.cardList().size() % this.getPlayers().size() != 0) 
			throw new AssertionError("Something is horribly wrong with the card count or player count. " + deck.cardList().size() + " modulo " + this.getPlayers().size());
		
		while (!deck.isEmpty()) {
			for (CardPlayer player : new CastedIterator<Player, CardPlayer>(this.getPlayers())) {
				Card card = deck.getTopCard();
				card.zoneMoveOnBottom(player.getHand());
			}
		}
		this.removeZone(deck);
		
		GamePhase phase = new HeartsGivePhase(this.giveDirection);
		this.setActivePhase(phase);
		if (!this.giveDirection.isGive()) {
			this.nextPhase();
		}
	}

	@Override
	public AIHandler getAIHandler() {
		return this.handler;
	}

	public void sort(ClassicCardZone zone) {
		CustomFacade.getLog().i("Sorting: " + zone);
		Collections.sort(zone.cardList(), compare);
	}
	
	private final Comparator<Card> compare = new ClassicCardComparator(new Suite[]{ Suite.CLUBS, Suite.DIAMONDS, Suite.SPADES, Suite.HEARTS }, true);
	
	public void scanForStartingCard() {
		for (Player player : this.getPlayers()) {
			CardPlayer pl = (CardPlayer) player;
			pl.getBoard().setGloballyKnown(true);
			if (!ZomisList.filter2(pl.getHand().cardList(), new ClassicCardFilter(Suite.CLUBS, 2)).isEmpty()) {
				setActivePlayer(pl);
			}
		}
	}
	
	
	private Map<Card, CardPlayer> lastPlayed = new HashMap<Card, CardPlayer>();
	
	@Override
	public boolean nextPhase() {
		if (!pile.isEmpty()) {
			ClassicCard model = (ClassicCard) pile.getTopCard().getModel();
			lastPlayed.put(pile.getTopCard(), getCurrentPlayer());
			if (model.getSuite() == Suite.HEARTS)
				this.heartsBroken = true;
		}
		
		if (pile.size() == this.getPlayers().size()) {
			givePileAndSelectNewPhase();
			return true;
		}
		else return super.nextPhase();
	}

	private void givePileAndSelectNewPhase() {
		Suite suite = null;
		int maxRank = 0;
		Card maxCard = null;
		
		ListIterator<Card> it = this.pile.cardList().listIterator();
		while (it.hasNext()) {
			Card card = it.next();
			ClassicCard model = (ClassicCard) card.getModel();
			if (suite == null || model.getSuite() == suite) {
				suite = model.getSuite();
				if (model.getRank() > maxRank) {
					maxRank = model.getRank();
					maxCard = card;
				}
			}
		}
		CardPlayer nextPlayer = this.lastPlayed.get(maxCard);
		LinkedList<Card> hearts = ZomisList.filter2(this.pile.cardList(), new ClassicCardFilter(Suite.HEARTS));
		LinkedList<Card> queen = ZomisList.filter2(this.pile.cardList(),  new ClassicCardFilter(Suite.SPADES, 12));
		hearts.addAll(queen);
		for (Card card : hearts) {
			card.zoneMoveOnBottom(nextPlayer.getBoard());
		}
		this.pile.moveToBottomOf(null);
//		this.pile.moveToTopOf(nextPlayer.getBoard());
		setActivePlayer(nextPlayer);
		
		this.lastPlayed.clear();
		
	}
	
	private void setActivePlayer(CardPlayer pl) {
		for (GamePhase phase : this.getPhases()) {
			PlayerPhase playerPhase = (PlayerPhase) phase;
			if (playerPhase.getPlayer() == pl) {
				if (this.getCurrentPlayer() != null)
					this.setActivePhase(phase);
				else this.setActivePhaseDirectly(phase);
			}
		}
	}
	
}
