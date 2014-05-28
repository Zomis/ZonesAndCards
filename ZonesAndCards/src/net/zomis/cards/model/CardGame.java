package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.events.game.GameOverEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.IPlayerPhase;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.EventConsumer;
import net.zomis.events.EventExecutor;
import net.zomis.events.EventListener;
import net.zomis.events.IEvent;
import net.zomis.events.IEventExecutor;
import net.zomis.events.IEventHandler;

public class CardGame<P extends Player, M extends CardModel> implements EventListener {
	
	private static class UselessAIHandler implements ActionHandler {
		@Override
		public StackAction click(Card<?> card) {
			return new InvalidStackAction("Useless Handler");
		}
		
		@Override
		public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
			ArrayList<Card<?>> a = new ArrayList<Card<?>>();
			return a;
		}
	}
	
	private final CardZone<Card<M>> actionZone;
	
	private ActionHandler actionHandler = new UselessAIHandler();
	
	protected void setActionHandler(ActionHandler aiHandler) {
		this.actionHandler = aiHandler;
	}
	
	public List<Card<?>> getUseableCards(Player player) {
		List<Card<?>> list = this.actionHandler.getUseableCards(this, player);
		list.addAll(this.actionZone.cardList());
		return list;
	}
	
	private final Map<String, M> availableCards = new HashMap<String, M>();
	private boolean callingOnEnd;
	private GamePhase currentPhase;
	private final IEventExecutor events;
	private Exception exc;
	private boolean gameOver = false;
	private final List<GamePhase> phases = new ArrayList<GamePhase>();
	private final List<P> players = new LinkedList<P>();
	private Random random = new Random();

	/**
	 * The Stack is used as a history manager as well.
	 * Possible stack actions: Activate a card, play card, discard card, next phase, auto-triggered effects, +more
	 */
	private final LinkedList<StackAction> stack = new LinkedList<StackAction>();;

	
	private boolean started;
	protected void resetStarted() {
		this.started = false;
	}
	
	public M getCardModel(String name) {
		return getCards().get(name);
	}

	private final List<CardZone<?>> zones = new ArrayList<CardZone<?>>();
	private CardReplay replay;

	private final Map<M, ActionProvider> actions;
	
	public CardGame() {
		if (!CustomFacade.isInitialized())
			throw new IllegalStateException("EventFactory not initialized.");
		this.events = CustomFacade.getInst().createEvents();
		this.actionZone = new CardZone<Card<M>>("Actions");
		this.actionZone.setGloballyKnown(true);
		this.addZone(actionZone);
		this.actions = new HashMap<M, ActionProvider>();
//		this.events.registerListener(this); // This breaks GWT
	}
	/**
	 * A combination of {@link #addStackAction(StackAction)} and {@link #processStackAction()}
	 * @param action {@link StackAction} to add and process.
	 */
	public void addAndProcessStackAction(StackAction action) {
		this.addStackAction(action);
		this.processStackAction();
	}

	public void addCard(M card) {
		if (card == null)
			throw new IllegalArgumentException("Card cannot be null");
		if (this.availableCards.containsKey(card.getName()))
			throw new IllegalStateException("A card with the name " + card.getName() + " has already been added");
		this.availableCards.put(card.getName(), card);
	}
	
	protected void addPhase(GamePhase phase) {
		this.phases.add(phase);
	}

	protected void addPlayer(P player) {
		this.players.add(player);
		player.game = this;
	}

	public Card<M> addAction(M actionModel, ActionProvider action) {
		addCard(actionModel);
		actions.put(actionModel, action);
		return this.actionZone.createCardOnBottom(actionModel);
	}
	
	/**
	 * Was meant to add a StackAction to the Stack and record it in history, but use instead CardGame.click(card) for that.
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
	
	protected final void endGame() {
		if (this.isGameOver()) {
			exc.printStackTrace();
			throw new IllegalStateException("Game is already finished, previously called at ", exc);
		}
		if (!this.executeEvent(new GameOverEvent(this)).isCancelled()) {
			exc = new Exception("End game called");
			this.gameOver = true;
		}
	}

	protected <T extends IEvent> T executeEvent(T event) {
		return getEvents().executeEvent(event);
	}
	protected void executeEvent(IEvent event, int i) {
		getEvents().executeEvent(event, i);
	}

	public GamePhase getActivePhase() {
		return this.currentPhase;
	}

	@Deprecated
	public Set<CardModel> getAvailableCards() {
		return new TreeSet<CardModel>(availableCards.values());
	}
	
	public Map<String, M> getCards() {
		return new HashMap<String, M>(availableCards);
	}
	
	@SuppressWarnings("unchecked")
	public P getCurrentPlayer() {
		GamePhase phase = getActivePhase();
		if (phase instanceof IPlayerPhase) {
			IPlayerPhase phase2 = (IPlayerPhase) phase;
			return (P) phase2.getPlayer();
		}
		return null;
	}

	protected IEventExecutor getEvents() {
		return events;
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
//		CustomFacade.getLog().i("Using random! " + new Exception().getStackTrace()[1]);
		return this.random;
	}

	protected LinkedList<StackAction> getStack() {
		return stack;
	}
	public final boolean isGameOver() {
		return gameOver;
	}
	public boolean isNextPhaseAllowed() {
		return true;
	}
	public boolean isStarted() {
		return started;
	}

	public boolean nextPhase() {
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
			int activePhase = this.phases.indexOf(getActivePhase());
			if (activePhase < 0) {
				throw new IllegalStateException("Phase does not appear in list of phases and did not change phase from GamePhase.onEnd: " + previousPhase);
			}
			int nextPhase = (activePhase + 1) % this.phases.size();
			GamePhase next = this.phases.get(nextPhase);
//			CustomFacade.getLog().i("Finding next phase in list: " + next);
			setActivePhaseDirectly(next); // onEnd already called above so it should not be called again
		}
		else {
			// setActivePhase was called from onEnd of the previous phase, no need to do anything here.
//			CustomFacade.getLog().d("Current phase is now " + this.getActivePhase());
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
//			return new InvalidStackAction("Game is not started. Did you forget to call startGame() ?");
		
		StackAction action = stack.isEmpty() ? null : stack.removeFirst();
		if (action == null) 
			action = new StackAction();
		
		if (action.actionIsAllowed()) {
//			CustomFacade.getLog().d("Action Perform: " + action);
			action.internalPerform();
			executeEvent(new AfterActionEvent(this, action));
		}
		else {
			action.onFailedPerform();
//			CustomFacade.getLog().d("StackAction was not allowed: " + action);
		}
		return action;
	}
	
	public void registerHandler(Class<? extends IEvent> eventType, IEventHandler handler) { // TODO: Deprecate CardGame.registerHandler
		getEvents().registerHandler(eventType, handler);
	}
	
	public <T extends IEvent> IEventHandler registerHandler(Class<? extends T> eventType, EventConsumer<T> handler) {
		return getEvents().registerHandler(eventType, handler);
	}
	
	public <T extends IEvent> IEventHandler registerHandler(Class<? extends T> eventType, EventConsumer<T> handler, int priority) {
		return getEvents().registerHandler(eventType, handler, priority);
	}
	
	@Deprecated
	public void registerListener(EventListener listener) {
		getEvents().registerListener(listener);
	}
	
	public void removeHandler(IEventHandler listener) {
		getEvents().removeHandler(listener);
	}
//	protected boolean removeZone(CardZone zone) {
//		return this.zones.remove(zone);
//	}
	
	protected void setActivePhase(GamePhase phase) {
		if (!callingOnEnd) {
			callingOnEnd = true;
			// TODO: There's gotta be a better solution for letting phases go to the next phase by themselves.
			GamePhase active = getActivePhase();
			if (active != null)
				active.onEnd(this);
		}
		callingOnEnd = false;
		setActivePhaseDirectly(phase);
	}
	protected void setActivePhaseDirectly(GamePhase phase) {
		GamePhase oldPhase = getActivePhase();
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, phase), EventExecutor.PRE);
		this.currentPhase = phase;
		GamePhase newPhase = getActivePhase();
		newPhase.onStart(this);
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, newPhase), EventExecutor.POST);
	}
	public final void setRandomSeed(long seed) {
		this.random = new Random(seed);
	}
	public final void setRandom(Random random) {
		this.random = random;
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
	
	public int stackSize() {
		return this.stack.size();
	}
	
	public boolean click(Card<?> card) {
		return clickPerform(card).actionIsPerformed();
	}
	
	public StackAction clickPerform(Card<?> card) {
		if (card == null)
			throw new NullPointerException("Card cannot be null");
		StackAction action = card.clickAction();
		if (action.actionIsAllowed()) {
			replay.addMove(card);
		}
		addAndProcessStackAction(action);
		return action;
	}
	
	public CardReplay getReplay() {
		return replay;
	}

	public StackAction getActionFor(Card<?> card) {
		if (actions.containsKey(card.getModel())) {
			return actions.get(card.getModel()).get();
		}
		return actionHandler.click(card);
	}

	public CardZone<?> getActionZone() {
		return this.actionZone;
	}

	public Player getFirstPlayer() {
		if (players.isEmpty())
			return null;
		return players.get(0);
	}
	
}
