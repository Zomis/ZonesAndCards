package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.events.AfterActionEvent;
import net.zomis.cards.events.GameOverEvent;
import net.zomis.cards.events.PhaseChangeEvent;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.IPlayerPhase;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.EventExecutor;
import net.zomis.events.EventListener;
import net.zomis.events.IEvent;

public class CardGame implements EventListener {
	
	private static class UselessAIHandler implements ActionHandler {
		@Override
		public StackAction click(Card card) {
			return new InvalidStackAction("Useless Handler");
		}
		
		@Override
		public List<StackAction> getAvailableActions(Player player) {
			return new ArrayList<StackAction>(0);
		}
	}
	
	private ActionHandler actionHandler = new UselessAIHandler();
	private final Set<CardModel> availableCards = new HashSet<CardModel>();
	private boolean callingOnEnd;
	private GamePhase currentPhase;
	private EventExecutor events = new EventExecutor();
	private Exception exc;
	private boolean gameOver = false;
	private final List<GamePhase> phases = new ArrayList<GamePhase>();
	private final List<Player> players = new LinkedList<Player>();
	private Random random = new Random();

	/**
	 * The Stack is used as a history manager as well.
	 * Possible stack actions: Activate a card, play card, discard card, next phase, auto-triggered effects, +more
	 */
	private final LinkedList<StackAction> stack = new LinkedList<StackAction>();;

	
	private boolean	started;
	
	private final Set<CardZone> zones = new HashSet<CardZone>();
	
	public CardGame() {
		this.events.registerListener(this);
	}
	/**
	 * A combination of {@link #addStackAction(StackAction)} and {@link #processStackAction()}
	 * @param action {@link StackAction} to add and process.
	 */
	public void addAndProcessStackAction(StackAction action) {
		this.addStackAction(action);
		this.processStackAction();
	}

	public void addCard(CardModel card) {
		this.availableCards.add(card);
	}
	
	protected void addPhase(GamePhase phase) {
		this.phases.add(phase);
	}

	protected void addPlayer(Player player) {
		this.players.add(player);
		player.game = this;
	}

	/**
	 * Adds a {@link StackAction} to the Stack and records it in history. <b>Should not be used for actions that trigger through other actions than an order by a player.</b>
	 * @param action Action to add to stack
	 */
	protected void addStackAction(StackAction action) {
		this.stack.push(action);
	}
	
	protected CardZone addZone(CardZone zone) {
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

	public ActionHandler getActionHandler() {
		return this.actionHandler;
	}
	
	public GamePhase getActivePhase() {
		return this.currentPhase;
	}

	public Set<CardModel> getAvailableCards() {
		return new TreeSet<CardModel>(availableCards);
	}
	
	public Player getCurrentPlayer() {
		GamePhase phase = getActivePhase();
		if (phase instanceof IPlayerPhase) {
			IPlayerPhase phase2 = (IPlayerPhase) phase;
			return phase2.getPlayer();
		}
		return null;
	}

	protected EventExecutor getEvents() {
		return events;
	}
	protected List<GamePhase> getPhases() {
		return phases;
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	public Set<CardZone> getPublicZones() {
		return new TreeSet<CardZone>(zones);
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
		if (previousPhase == null)
			this.setActivePhase(this.phases.get(0));
		else previousPhase.onEnd(this);
		
		if (previousPhase == this.getActivePhase()) {
			int activePhase = this.phases.indexOf(getActivePhase());
			if (activePhase < 0) {
				throw new IllegalStateException("Phase does not appear in list of phases and did not change phase from onEnd: " + previousPhase);
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
			return new InvalidStackAction("Game is not started. Did you forget to call startGame() ?");
		
		StackAction action = this.stack.pollFirst();
		if (action == null) 
			action = new StackAction();
		
		if (action.actionIsAllowed()) {
//			CustomFacade.getLog().d("Action Perform: " + action);
			action.internalPerform();
			executeEvent(new AfterActionEvent(this, action));
		}
		else CustomFacade.getLog().d("StackAction was not allowed: " + action);
		return action;
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
	protected void setActionHandler(ActionHandler aiHandler) {
		this.actionHandler = aiHandler;
	}
	
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
		this.currentPhase = phase;
		GamePhase newPhase = getActivePhase();
		newPhase.onStart(this);
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, newPhase));
	}
	public final void setRandomSeed(long seed) {
//		CustomFacade.getLog().i("Set seed to " + seed);
		this.random = new Random(seed);
	}
	public final void startGame() {
		if (this.started)
			throw new IllegalStateException("Game is already initialized.");
		this.started = true;
		this.onStart();
		if (this.getActivePhase() == null) {
			this.setActivePhase(this.phases.get(0));
		}
	}
}
