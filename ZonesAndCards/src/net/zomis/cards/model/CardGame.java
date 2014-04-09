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

import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.cards.events.game.AfterActionEvent;
import net.zomis.cards.events.game.GameOverEvent;
import net.zomis.cards.events.game.PhaseChangeEvent;
import net.zomis.cards.hstone.factory.HStoneCardModel;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.IPlayerPhase;
import net.zomis.custommap.CustomFacade;
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
		public <E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(E cardGame, Player player) {
			return new ArrayList<StackAction>(0);
		}

		@Override
		public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
			ArrayList<Card<?>> a = new ArrayList<Card<?>>();
			CardModel cm = new HStoneCardModel(null, 0, null);
			a.add(cm.createCardInternal(null));
			return a;
		}
	}
	
	private final CardZone<Card<M>> actionZone;
	
	private ActionHandler actionHandler = new UselessAIHandler();
	
	protected void setActionHandler(ActionHandler aiHandler) {
		this.actionHandler = aiHandler;
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public <E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(Player player) {
		return this.actionHandler.getAvailableActions((E) this, player);
	}
	
	public List<Card<?>> getUseableCards(Player player) {
		return this.actionHandler.getUseableCards(this, player);
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
	
	private final List<CardZone<?>> zones = new ArrayList<CardZone<?>>();
	private CardReplay replay;
	
	public CardGame() {
		if (!CustomFacade.isInitialized())
			throw new IllegalStateException("EventFactory not initialized.");
		this.events = CustomFacade.getInst().createEvents();
		this.actionZone = new CardZone<Card<M>>("Actions");
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
		return this.actionZone.createCardOnBottom(actionModel);
	}
	
	/**
	 * Was meant to add a StackAction to the Stack and record it in history, but use instead CardGame.click(card) for that.
	 * @param action Action to add to stack
	 */
	@Deprecated
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
	public StackAction callPlayerAI() {
		return this.callPlayerAI(this.getCurrentPlayer());
	}
	public StackAction callPlayerAI(CardAI ai) {
		return this.callPlayerAI(getCurrentPlayer(), ai);
	}
	public StackAction callPlayerAI(Player player) {
		if (player == null) {
			// TODO: Codecrap for callPlayerAI when there is no current player. The returned stackaction here is used to determine if something has happened in CardGame.autoplay.
			StackMultiAction action = new StackMultiAction();
			for (Player pl : this.getPlayers()) {
				action.addAction(this.callPlayerAI(pl));
			}
			return action;
		}
		else {
			return this.callPlayerAI(player, player.getAI());
		}
	}
	
	public StackAction callPlayerAI(Player player, CardAI ai) {
		if (ai == null)
			return new InvalidStackAction("No AI specified to use for " + player);
		ParamAndField<Player, StackAction> action = ai.play(player);
		StackAction field = action.getField();
		this.addAndProcessStackAction(field);
		return action.getField();
	}

	protected final void endGame() {
		if (this.isGameOver()) {
			exc.printStackTrace();
			throw new IllegalStateException("Game is already finished, previously called at ", exc);
		}
		if (!this.getEvents().executeEvent(new GameOverEvent(this)).isCancelled()) {
			exc = new Exception("End game called");
			this.gameOver = true;
		}
	}

	protected void executeEvent(IEvent event) {
		getEvents().executeEvent(event);
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
	
	public Player getCurrentPlayer() {
		GamePhase phase = getActivePhase();
		if (phase instanceof IPlayerPhase) {
			IPlayerPhase phase2 = (IPlayerPhase) phase;
			return phase2.getPlayer();
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
			CustomFacade.getLog().d("StackAction was not allowed: " + action);
		}
		return action;
	}
	
	public void registerHandler(Class<? extends IEvent> eventType, IEventHandler handler) {
		getEvents().registerHandler(eventType, handler);
	}
	
	public void registerListener(EventListener listener) {
		getEvents().registerListener(listener);
	}
	
	public void removeListener(EventListener listener) {
		getEvents().removeListener(listener);
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
			throw new IllegalStateException("Game is already initialized.");
		this.replay = new CardReplay(this);
		this.started = true;
		this.onStart();
		if (this.getActivePhase() == null) {
			this.setActivePhase(this.phases.get(0));
		}
	}
	public void setAI(int playerIndex, CardAI ai) {
		this.getPlayers().get(playerIndex).setAI(ai);
	}
	public void autoplay() {
		
		while (!this.isGameOver()) {
			StackAction action = this.callPlayerAI();
			if (!action.actionIsPerformed())
				return;
		}
		
	}
	
	public int stackSize() {
		return this.stack.size();
	}
	
	public boolean click(Card<?> card) {
		StackAction action = card.clickAction();
		if (action.actionIsAllowed()) {
			replay.addMove(card);
		}
		// TODO: If action is allowed, add info about card zone and card index in zone to replay data
		addAndProcessStackAction(action);
		return action.actionIsPerformed();
	}
	
	public CardReplay getReplay() {
		return replay;
	}

	public StackAction getActionFor(Card<?> card) {
		return actionHandler.click(card);
	}
}
