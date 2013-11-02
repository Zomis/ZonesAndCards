package net.zomis.cards.idiot;

import net.zomis.ArrayIterator;
import net.zomis.IndexIterator;
import net.zomis.IndexIteratorStatus;
import net.zomis.ZomisList;
import net.zomis.ZomisList.FilterInterface;
import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.classics.Suite;
import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.util.StackActionAllowedFilter;
import net.zomis.events.Event;

public class IdiotGame extends ClassicGame {

	private final ClassicCardZone[] zones;
	private ClassicCardZone	deck;
	private static final FilterInterface<StackAction> allowedActionFilter = new StackActionAllowedFilter(true);
	
	public IdiotGame() {
		super(AceValue.HIGH);
		this.setActionHandler(new IdiotHandler());
		this.zones = new ClassicCardZone[Suite.suiteCount(false)];
		this.deck = new ClassicCardZone("Deck");
		addZone(deck);
		for (IndexIteratorStatus<ClassicCardZone> zz : new IndexIterator<ClassicCardZone>(new ArrayIterator<ClassicCardZone>(zones))) {
			ClassicCardZone zone = new ClassicCardZone("Zone" + zz.getIndex());
			zones[zz.getIndex()] = zone;
			addZone(zone);
			zone.setGloballyKnown(true);
		}
		
		this.deck.addDeck(this, 0);
		CardPlayer player = new CardPlayer();
		this.addPlayer(player);
		this.addPhase(new PlayerPhase(player));
	}
	
	public ClassicCardZone getDeck() {
		return deck;
	}
	public ClassicCardZone[] getIdiotZones() {
		return zones;
	}
	
	@Event
	public void afterAction(AfterActionEvent event) {
		if (ZomisList.filter2(this.getAvailableActions(this.getCurrentPlayer()), allowedActionFilter).isEmpty())
			this.endGame();
	}
	
	public int getCardsLeft() {
		int i = 0;
		if (!this.deck.cardList().isEmpty())
			return Integer.MAX_VALUE;
		
		for (CardZone zone : this.zones) {
			i += zone.cardList().size();
		}
		
		return i;
	}
	
	@Override
	public void onStart() {
		this.deck.shuffle();
	}
	
}
