package net.zomis.cards.idiot;

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
import net.zomis.events.EventHandlerGWT;
import net.zomis.iterate.ArrayIterator;
import net.zomis.iterate.IndexIterator;
import net.zomis.iterate.IndexIteratorStatus;
import net.zomis.utils.ZomisList;
import net.zomis.utils.ZomisList.FilterInterface;

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
		this.registerHandler(AfterActionEvent.class, new EventHandlerGWT<AfterActionEvent>() {
			@Override
			public void executeEvent(AfterActionEvent event) {
				onAfterAction(event);
			}
		});
	}
	
	public ClassicCardZone getDeck() {
		return deck;
	}
	public ClassicCardZone[] getIdiotZones() {
		return zones;
	}
	
	private void onAfterAction(AfterActionEvent event) {
		if (ZomisList.filter2(this.getAvailableActions(this.getCurrentPlayer()), allowedActionFilter).isEmpty())
			this.endGame();
	}
	
	public int getCardsLeft() {
		int i = 0;
		if (!this.deck.cardList().isEmpty())
			return Integer.MAX_VALUE;
		
		for (CardZone<?> zone : this.zones) {
			i += zone.cardList().size();
		}
		
		return i;
	}
	
	@Override
	public void onStart() {
		this.deck.shuffle();
	}
	
}
