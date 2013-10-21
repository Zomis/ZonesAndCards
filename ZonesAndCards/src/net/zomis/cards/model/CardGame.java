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
	
	public CardGame() {
		this.events.registerListener(this);
	}
	
	private Random random = new Random();
	private boolean gameOver = false;
	
	private final List<Player> players = new LinkedList<Player>();
	
	private final List<GamePhase> phases = new ArrayList<GamePhase>();
	/**
	 * Possible stack actions: Activate ability, play spell, play land, next phase, auto-triggered effects, +more
	 * Stack actions in Castle Wars: Play card, discard cards
	 */
	private final LinkedList<StackAction> stack = new LinkedList<StackAction>();
	
	protected LinkedList<StackAction> getStack() {
		return stack;
	}
	
	private final Set<CardZone> zones = new HashSet<CardZone>();
	private final Set<CardModel> availableCards = new HashSet<CardModel>();
	
	private GamePhase currentPhase;

	private boolean	started;

	private static class UselessAIHandler implements AIHandler {
		@Override
		public List<StackAction> getAvailableActions(Player player) {
			return new ArrayList<StackAction>(0);
		}
		
		@Override
		public StackAction click(Card card) {
			return new InvalidStackAction();
		}
	};

	
	private AIHandler aiHandler = new UselessAIHandler();
	
	protected void setAIHandler(AIHandler aiHandler) {
		this.aiHandler = aiHandler;
	}
	
	public Set<CardModel> getAvailableCards() {
		return new TreeSet<CardModel>(availableCards);
	}
	
	protected void addPlayer(Player player) {
		this.players.add(player);
		player.game = this;
	}

	public Set<CardZone> getPublicZones() {
		return new TreeSet<CardZone>(zones);
	}
	
	protected CardZone addZone(CardZone zone) {
		this.zones.add(zone);
		zone.game = this;
		return zone;
	}

	public void addCard(CardModel card) {
		this.availableCards.add(card);
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}
	
	protected List<GamePhase> getPhases() {
		return phases;
	}
	protected void setActivePhaseDirectly(GamePhase phase) {
		GamePhase oldPhase = getActivePhase();
		this.currentPhase = phase;
		GamePhase newPhase = getActivePhase();
		newPhase.onStart(this);
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, newPhase));
	}
	private boolean callingOnEnd;
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
	
	public GamePhase getActivePhase() {
		return this.currentPhase;
	}

	protected void addPhase(GamePhase phase) {
		this.phases.add(phase);
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

	private EventExecutor events = new EventExecutor();
	
	protected EventExecutor getEvents() {
		return events;
//		return CustomFacade.getGlobalEvents();
	}

	public boolean isStarted() {
		return started;
	}
	
	public StackAction processStackAction() {
		if (!this.started)
			throw new IllegalStateException("Game is not started. Did you forget to call startGame() ?");
		
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

	protected void addStackAction(StackAction action) {
		this.stack.push(action);
	}
	public void addAndProcessStackAction(StackAction action) {
		this.addStackAction(action);
		this.processStackAction();
	}

	public AIHandler getAIHandler() {
		return this.aiHandler;
	}

	public Random getRandom() {
		return this.random;
	}
	
	
	public void setRandomSeed(long seed) {
		this.random = new Random(seed);
	}

	public void registerListener(EventListener listener) {
		getEvents().registerListener(listener);
	}
	public void removeListener(EventListener listener) {
		getEvents().removeListener(listener);
	}
	protected void executeEvent(IEvent event) {
		getEvents().executeEvent(event);
	}
	public Player getCurrentPlayer() {
		GamePhase phase = getActivePhase();
		if (phase instanceof IPlayerPhase) {
			IPlayerPhase phase2 = (IPlayerPhase) phase;
			return phase2.getPlayer();
		}
		return null;
	}

	public boolean isNextPhaseAllowed() {
		return true;
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

	protected void onStart() {}
	
	protected boolean removeZone(CardZone zone) {
		return this.zones.remove(zone);
	}
	
	public StackAction callPlayerAI() {
		return this.callPlayerAI(this.getCurrentPlayer());
	}
	public StackAction callPlayerAI(CardAI ai) {
		return this.callPlayerAI(getCurrentPlayer(), ai);
	}
	public StackAction callPlayerAI(Player player, CardAI ai) {
		if (ai == null)
			return new InvalidStackAction("No AI specified to use for " + player);
		ParamAndField<Player, StackAction> action = ai.play(player);
		StackAction field = action.getField();
		this.addAndProcessStackAction(field);
		return action.getField();
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
	public boolean isGameOver() {
		return gameOver;
	}
	protected void endGame() {
		if (this.isGameOver())
			throw new IllegalStateException("Game is already finished.");
		if (!this.getEvents().executeEvent(new GameOverEvent(this)).isCancelled()) {
//			new Exception("End game called").printStackTrace();
			this.gameOver = true;
		}
	}
}
