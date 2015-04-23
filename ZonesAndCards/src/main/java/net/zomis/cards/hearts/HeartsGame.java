package net.zomis.cards.hearts;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardComparator;
import net.zomis.cards.classics.ClassicCardFilter;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.GamePhase;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.utils.ZomisList;

public class HeartsGame extends ClassicGame {
	public static final int RANKS_PER_SUITE = 52 / 4;
	
	private final Comparator<Card<ClassicCard>> compare = new ClassicCardComparator(new Suite[]{ Suite.CLUBS, Suite.DIAMONDS, Suite.SPADES, Suite.HEARTS }, true);
	private final ClassicCardZone pile;
	private final Map<Card<ClassicCard>, CardPlayer> lastPlayed = new HashMap<Card<ClassicCard>, CardPlayer>();
	
	protected HeartsGiveDirection	giveDirection;
	private boolean heartsBroken;

	private Card<ClassicCard>	nextTurnCard;
	
	public HeartsGame(HeartsGiveDirection giveDirection) {
		super(AceValue.HIGH);
		this.nextTurnCard = this.addAction(new ClassicCard(Suite.EXTRA, 0), () -> new NextTurnAction(HeartsGame.this));
		this.setActionHandler(new HeartsHandler());
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
		ClassicCardZone hand = new ClassicCardZone("Hand", player);
		hand.setKnown(player, true);
		player.setHand(hand);
		this.addZone(hand);
		
		ClassicCardZone board = new ClassicCardZone("GiveAway--TakenCards", player);
		board.setGloballyKnown(false);
		board.setKnown(player, true);
		player.setBoard(board);
		this.addZone(board);
		
		this.addPhase(new GamePhase(player));
		super.addPlayer(player);
		return this;
	}
	
	@Override
	protected void onStart() {
		this.heartsBroken = false;
		this.pile.moveToBottomOf(null);
		ClassicCardZone deck = new ClassicCardZone("Deck");
		deck.addDeck(this, 0);
		if (deck.size() % this.getPlayers().size() != 0) 
			throw new IllegalStateException("Something is horribly wrong with the card count or player count. " + deck.size() + " modulo " + this.getPlayers().size());
		if (this.getPlayers().size() != 4)
			throw new IllegalStateException("Hearts must currently be played with 4 players.");
		
		deck.shuffle(this.getRandom());
		dealUntilLeft(deck, 0, this.getPlayers(), new CardPlayer.GetHand());
		
		GamePhase phase = new HeartsGivePhase(this.giveDirection);
		this.setActivePhase(phase);
		if (!this.giveDirection.isGive()) {
			this.nextPhase();
		}
	}

	public static interface GetZoneInterface<E> {
		<C extends Card<?>> CardZone<C> getZone(E object);
	}
	
	public void dealUntilLeft(ClassicCardZone deck, int cardsLeft, List<CardPlayer> players, GetZoneInterface<CardPlayer> getHand) {
		while (true) {
			for (CardPlayer player : players) {
				if (deck.size() <= cardsLeft)
					return;
				Card<ClassicCard> card = deck.getTopCard();
				card.zoneMoveOnBottom(getHand.getZone(player));
			}
		}
	}
	
	public void sort(ClassicCardZone zone) {
//		CustomFacade.getLog().d("Sorting: " + zone);
		zone.sort(compare);
	}
	
	
	public void scanForStartingCard() {
		final ClassicCard startCard = new ClassicCard(Suite.CLUBS, 2);
		for (CardPlayer player : this.getPlayers()) {
			player.getBoard().setGloballyKnown(true);
			sort(player.getHand());
			if (player.getHand().containsModel(startCard)) { //) !ZomisList.fil getAll(pl.getHand(), new ClassicCardFilter(Suite.CLUBS, 2)).isEmpty()) {
				setActivePlayer(player);
			}
		}
	}
	
	
	
	@Override
	public boolean isNextPhaseAllowed() {
		boolean hasCurrentPlayer = getCurrentPlayer() != null;
		if (this.giveDirection.isGive() && !hasCurrentPlayer) {
			for (Player pl : this.getPlayers()) {
				CardPlayer player = (CardPlayer) pl;
				if (HeartsGiveAction.GIVE_COUNT != player.getBoard().size())
					return false;
			}
		}
		if (hasCurrentPlayer && !lastPlayed.containsValue(getCurrentPlayer())) {
			return false;
		}
		return super.isNextPhaseAllowed();
	}
	
	public void informMove(Card<ClassicCard> card) {
		if (!pile.isEmpty()) {
			ClassicCard model = card.getModel();
			lastPlayed.put(card, getCurrentPlayer());
			if (model.getSuite() == Suite.HEARTS)
				this.heartsBroken = true;
		}
		else System.out.println("WARNING: Pile not empty. " + pile.cardList() + " card " + card);
	}
	
	@Override
	public boolean nextPhase() {
		
		if (pile.size() == this.getPlayers().size()) {
			givePileAndSelectNewPhase();
			return true;
		}
		else return super.nextPhase();
	}

	private void givePileAndSelectNewPhase() {
		Suite suite = null;
		int maxRank = 0;
		Card<ClassicCard> maxCard = null;
		
		// Find out who will get the current stick
		ListIterator<Card<ClassicCard>> it = this.pile.cardList().listIterator();
		while (it.hasNext()) {
			Card<ClassicCard> card = it.next();
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
		if (nextPlayer == null)
			throw new IllegalStateException("Next player was null. Last played is " + lastPlayed + " card is " + maxCard);
		
		// Find all the cards that give points
		List<Card<ClassicCard>> hearts = ZomisList.getAll(this.pile.cardList(), new ClassicCardFilter(Suite.HEARTS));
		List<Card<ClassicCard>> queen = ZomisList.getAll(this.pile.cardList(), new ClassicCardFilter(Suite.SPADES, ClassicCard.RANK_QUEEN));
		hearts.addAll(queen);
		for (Card<ClassicCard> card : hearts) {
			// give the point cards to the player who won the stick
			card.zoneMoveOnBottom(nextPlayer.getBoard());
		}
//		this.pile.moveToBottomOf(null); // move the rest of the cards to /dev/null
		
		setActivePlayer(nextPlayer);
		if (this.getCurrentPlayer().getHand().isEmpty())
			this.endGame(); // if there are no more cards to play, then game is over
		
		this.lastPlayed.clear();
	}
	
	private void setActivePlayer(CardPlayer pl) {
		for (GamePhase phase : this.getPhases()) {
			if (phase.getPlayer() == pl) {
				if (this.getCurrentPlayer() != null)
					this.setActivePhase(phase);
				else this.setActivePhaseDirectly(phase);
			}
		}
	}
	
	public int calcRealPoints(CardPlayer player) {
		CardPlayer gotAll = playerGotAll();
		if (gotAll != null) {
			return player == gotAll ? 0 : 26; 
		}
		else return getPoints(player);
	}
	private CardPlayer playerGotAll() {
		for (Player i : this.getPlayers()) {
			if (getPoints((CardPlayer) i) == 26)
				return (CardPlayer) i;
		}
		return null;
	}
	private int getPoints(CardPlayer player) {
		if (this.getCurrentPlayer() == null)
			return 0;
		int i = 0;
		i += ZomisList.getAll(player.getBoard().cardList(), new ClassicCardFilter(Suite.HEARTS)).size();
		i += RANKS_PER_SUITE * ZomisList.getAll(player.getBoard().cardList(), new ClassicCardFilter(Suite.SPADES, ClassicCard.RANK_QUEEN)).size();
		return i;
	}
	
	public Card<ClassicCard> getNextTurnCard() {
		return nextTurnCard;
	}
	
}
