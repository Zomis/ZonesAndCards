package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.zomis.cards.events.card.CardPlayedEvent;
import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.events.game.GameOverEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.interfaces.ActionHandler;
import net.zomis.cards.interfaces.ActionProvider;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.util.CardSet;
import net.zomis.events.EventConsumer;
import net.zomis.events.EventExecutorGWT;
import net.zomis.events.EventExecutorOrderedByInsert;
import net.zomis.events.EventHandlerGWT;
import net.zomis.events.EventListener;
import net.zomis.events.IEvent;
import net.zomis.events.IEventExecutor;
import net.zomis.events.IEventHandler;

public class CardGame<P extends Player, M extends CardModel> implements EventListener {
	
	private ActionHandler actionHandler;
	
	private final Map<M, ActionProvider> actions;
	private final CardZone<Card<M>> actionZone;
	
	private final Map<String, M> availableCards = new HashMap<String, M>();
	
	private boolean callingOnEnd;
	
	private GamePhase currentPhase;
	private final IEventExecutor events;
	private boolean gameOver = false;
	private final List<GamePhase> phases = new ArrayList<GamePhase>();
	private final List<P> players = new LinkedList<P>();
	private Random random = new Random();
	private CardReplay replay;
	
	/**
	 * The stack provides a way for actions to be processed one at a time.
	 * Cannot be declared as a Deque interface because of GWT.
	 */
	private final LinkedList<StackAction> stack = new LinkedList<StackAction>();

	private boolean started;

	private final List<CardZone<?>> zones = new ArrayList<CardZone<?>>();

	public CardGame() {
		this.events = new EventExecutorOrderedByInsert();
		this.actionZone = new CardZone<Card<M>>("Actions");
		this.actionZone.setGloballyKnown(true);
		this.addZone(actionZone);
		this.actions = new HashMap<M, ActionProvider>();
	}
	
	public Card<M> addAction(M actionModel, ActionProvider action) {
		addCard(actionModel);
		actions.put(actionModel, action);
		return this.actionZone.createCardOnBottom(actionModel);
	}

	/**
	 * A combination of {@link #addStackAction(StackAction)} and {@link #processStackAction()}
	 * @param action {@link StackAction} to add and process.
	 */
	public void addAndProcessStackAction(StackAction action) {
		this.addStackAction(action);
		this.processStackAction();
	}
	
	public void addCard(M cardModel) {
		if (cardModel == null)
			throw new IllegalArgumentException("Card cannot be null");
		if (this.availableCards.containsKey(cardModel.getName()))
			throw new IllegalStateException("A card with the name " + cardModel.getName() + " has already been added");
		this.availableCards.put(cardModel.getName(), cardModel);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CardGame<P, M>> void addCards(CardSet<T> cardSet) {
		cardSet.addCards((T) this);
	}

	protected void addPhase(GamePhase phase) {
		this.phases.add(phase);
	}
	
	protected void addPlayer(P player) {
		this.players.add(player);
		player.game = this;
	}

	/**
	 * Add an action to the stack to be performed later. This does not save anything in history.
	 * @param action Action to add to stack
	 */
	public void addStackAction(StackAction action) {
		this.stack.addFirst(action);
	}

	protected CardZone<?> addZone(CardZone<?> zone) {
		if (zone == null)
			throw new IllegalArgumentException("Zone cannot be null");
		this.zones.add(zone);
		zone.game = this;
		return zone;
	}
	
	public boolean click(Card<?> card) {
		return clickPerform(card).actionIsPerformed();
	}
	
	public StackAction clickPerform(Card<?> card) {
		if (card == null)
			throw new NullPointerException("Card cannot be null");
		
		CardGame<?, ?> cardGame = card.getGame();
		P player = getCurrentPlayer();
		StackAction action = card.clickAction();
		if (action.actionIsAllowed()) {
			getReplay().addMove(card);
		}
		addAndProcessStackAction(action);
		executeEvent(new CardPlayedEvent(card, player, cardGame, action));
		return action;
	}
	
	protected final void endGame() {
		if (this.isGameOver()) {
			return;
		}
		if (!this.executeEvent(new GameOverEvent(this)).isCancelled()) {
			this.gameOver = true;
		}
	}

	protected void executeEvent(IEvent event, int i) {
		getEvents().executeEvent(event, i);
	}
	protected <T extends IEvent> T executeEvent(T event) {
		return getEvents().executeEvent(event);
	}

	protected <T extends IEvent> T executeEvent(T event, Runnable runInBetween) {
		executeEvent(event, EventExecutorGWT.PRE);
		runInBetween.run();
		executeEvent(event, EventExecutorGWT.POST);
		return event;
	}

	public StackAction getActionFor(Card<?> card) {
		if (actionHandler == null)
			throw new IllegalStateException("No actionHandler has been set.");
		if (actions.containsKey(card.getModel())) {
			return actions.get(card.getModel()).get();
		}
		return actionHandler.click(card);
	}
	
	public CardZone<?> getActionZone() {
		return this.actionZone;
	}

	public GamePhase getActivePhase() {
		return this.currentPhase;
	}
	public M getCardModel(String name) {
		return getCards().get(name);
	}

	public Map<String, M> getCards() {
		return new HashMap<String, M>(availableCards);
	}

	@SuppressWarnings("unchecked")
	public P getCurrentPlayer() {
		GamePhase phase = getActivePhase();
		return (P) phase.getPlayer();
	}
	
	protected IEventExecutor getEvents() {
		return events;
	}

	public Player getFirstPlayer() {
		if (players.isEmpty())
			return null;
		return players.get(0);
	}
	
	protected List<GamePhase> getPhases() {
		return phases;
	}
	
	public List<P> getPlayers() {
		return Collections.unmodifiableList(players);
	}
	
	public List<CardZone<?>> getPublicZones() {
		return new ArrayList<CardZone<?>>(zones);
	}

	public final Random getRandom() {
		return this.random;
	}
	
	public CardReplay getReplay() {
		return replay;
	}

	protected List<StackAction> getStack() {
		return stack;
	}
	
	public List<Card<?>> getUseableCards(Player player) {
		if (actionHandler == null)
			throw new IllegalStateException("No actionHandler has been set.");
		List<Card<?>> list = this.actionHandler.getUseableCards(this, player);
		list.addAll(this.actionZone.cardList());
		return list;
	}
	
	public final boolean isGameOver() {
		return gameOver;
	}
	
	public boolean isNextPhaseAllowed() {
		return stack.isEmpty();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean nextPhase() {
		if (!this.started)
			throw new IllegalStateException("Game is not started.");
		if (!this.isNextPhaseAllowed())
			return false;
		
		GamePhase previousPhase = getActivePhase();
		if (previousPhase == null) {
			this.setActivePhase(this.phases.get(0));
		}
		else {
			callingOnEnd = true;
			previousPhase.onEnd(this);
			callingOnEnd = false;
		}
		
		if (previousPhase == this.getActivePhase()) {
			// setActivePhase was not called from onEnd of the previous phase, so we need to find the next phase here
			int activePhase = this.phases.indexOf(getActivePhase());
			if (activePhase < 0) {
				throw new IllegalStateException("Phase does not appear in list of phases and did not change phase from GamePhase.onEnd: " + previousPhase);
			}
			int nextPhase = (activePhase + 1) % this.phases.size();
			GamePhase next = this.phases.get(nextPhase);
			setActivePhaseDirectly(next); // onEnd already called above so it should not be called again
		}
		return true;
	}
	
	protected void onStart() {}
	
	/**
	 * Take the top {@link StackAction} of the stack and process it if it is allowed.
	 * @return The {@link StackAction} that was removed from the stack and possibly processed.
	 */
	public StackAction processStackAction() {
		if (this.isGameOver())
			return new InvalidStackAction("Game has already ended.");
		if (!this.started)
			throw new IllegalStateException("Game is not started. Did you forget to call startGame() ?");
		
		StackAction action = stack.isEmpty() ? null : stack.removeFirst();
		if (action == null) 
			action = new StackAction();
		
		if (action.actionIsAllowed()) {
			action.internalPerform();
			executeEvent(new AfterActionEvent(this, action));
		}
		else {
			action.onFailedPerform();
		}
		return action;
	}
	
	public void registerHandler(Class<? extends IEvent> eventType, EventHandlerGWT<? extends IEvent> handler) {
		getEvents().registerHandler(eventType, handler);
	}
	
	public <T extends IEvent> IEventHandler registerHandler(Class<? extends T> eventType, EventConsumer<T> handler) {
		return getEvents().registerHandler(eventType, handler);
	}
	
	public <T extends IEvent> IEventHandler registerHandler(Class<? extends T> eventType, EventConsumer<T> handler, int priority) {
		return getEvents().registerHandler(eventType, handler, priority);
	}
	
	public void removeHandler(IEventHandler listener) {
		getEvents().removeHandler(listener);
	}
	
	protected void setActionHandler(ActionHandler aiHandler) {
		if (aiHandler == null)
			throw new IllegalArgumentException("ActionHandler cannot be null.");
		this.actionHandler = aiHandler;
	}
	
	protected void setActivePhase(GamePhase phase) {
		if (!callingOnEnd) {
			callingOnEnd = true;
			// TODO: The `callingOnEnd` variable feels like a little dirty way to let phases go to the next phase by themselves, but it works.
			GamePhase active = getActivePhase();
			if (active != null)
				active.onEnd(this);
		}
		callingOnEnd = false;
		setActivePhaseDirectly(phase);
	}

	/**
	 * Go to the next phase without calling onEnd on the current phase. Will call onStart on the new phase.
	 * @param phase The phase to go to.
	 */
	protected void setActivePhaseDirectly(GamePhase phase) {
		GamePhase oldPhase = getActivePhase();
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, phase), EventExecutorGWT.PRE);
		this.currentPhase = phase;
		GamePhase newPhase = getActivePhase();
		newPhase.onStart(this);
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, newPhase), EventExecutorGWT.POST);
	}

	public final void setRandom(Random random) {
		this.random = random;
	}

	public final void setRandomSeed(long seed) {
		this.random = new Random(seed);
	}

	public int stackSize() {
		return this.stack.size();
	}
	
	public final void startGame() {
		if (this.started)
			throw new IllegalStateException("Game is already started.");
		if (this.phases.isEmpty())
			throw new IllegalStateException("Game does not have any phases");
		this.replay = new CardReplay(this);
		this.started = true;
		this.onStart();
		if (this.getActivePhase() == null) {
			this.setActivePhase(this.phases.get(0));
		}
	}
}
