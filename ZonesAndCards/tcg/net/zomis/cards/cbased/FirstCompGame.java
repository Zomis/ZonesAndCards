package net.zomis.cards.cbased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.events2.GameStartedEvent;
import net.zomis.cards.iface.Component;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.GamePhase;
import net.zomis.cards.model.StackAction;
import net.zomis.events.CancellableEvent;
import net.zomis.events.EventExecutorGWT;
import net.zomis.events.IEvent;

public class FirstCompGame extends CardGame<CompPlayer, CompCardModel> implements HasComponents {

	private final List<GameSystem> systems = new ArrayList<>(); // List or Set? Should the order matter?
	private final Map<Class<? extends Component>, Component> components = new HashMap<>();
	
	public FirstCompGame() {
		this.setActionHandler(new ComponentHandler());
	}
	
	public void addSystem(GameSystem system) {
		this.systems.add(system);
	}
	
	@Override
	public CardZone<?> addZone(CardZone<?> zone) {
		return super.addZone(zone);
	}
	
	@Override
	protected void onStart() {
		for (GameSystem system : systems)
			system.onStart(this);
		
		this.executeEvent(new GameStartedEvent(this));
	}
	
	public <T extends CancellableEvent> T executeCancellableEvent(T event, Runnable runInBetween) {
		executeEvent(event, EventExecutorGWT.PRE);
		if (event.isCancelled())
			return event;
		runInBetween.run();
		executeEvent(event, EventExecutorGWT.POST);
		return event;
	}
	
	@Override
	public <T extends IEvent> T executeEvent(T event, Runnable runInBetween) {
		return super.executeEvent(event, runInBetween);
	}
	
	public <T extends IEvent> T executeEvent(T event) {
		return getEvents().executeEvent(event);
	}
	
	@Override
	public void addPlayer(CompPlayer player) {
		super.addPlayer(player);
	}
	
	@Override
	public void addPhase(GamePhase phase) {
		super.addPhase(phase);
	}

	@Override
	public Map<Class<? extends Component>, Component> getComponents() {
		return components;
	}

	public void gameOver() {
		super.endGame();
	}
	
	@Override
	public StackAction clickPerform(Card<?> card) {
		if (card == null)
			throw new NullPointerException("Card cannot be null");
		
		CardGame<?, ?> cardGame = card.getGame();
		CompPlayer player = getCurrentPlayer();
		StackAction action = card.clickAction();
		if (action.actionIsAllowed()) {
			getReplay().addMove(card);
		}
		addAndProcessStackAction(action);
		executeEvent(new CardPlayedEvent(card, player, cardGame, action));
		return action;
	}
}
