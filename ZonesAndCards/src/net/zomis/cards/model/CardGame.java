package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.zomis.cards.events.PhaseChangeEvent;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.IPlayerPhase;
import net.zomis.cards.swing.AfterActionEvent;
import net.zomis.custommap.CustomFacade;
import net.zomis.events.EventExecutor;
import net.zomis.events.EventListener;
import net.zomis.events.IEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class CardGame {
	
	private Random random = new Random();

	@JsonManagedReference
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
	
	@JsonManagedReference
	private final Set<CardZone> zones = new HashSet<CardZone>();
	private final Set<CardModel> availableCards = new HashSet<CardModel>();
	
	private GamePhase currentPhase;

	private boolean	started;
	
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

	protected void addCard(CardModel card) {
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
	protected void setActivePhase(GamePhase phase) {
		GamePhase active = getActivePhase();
		if (active != null)
			active.onEnd(this);
		
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
			setActivePhase(this.phases.get(nextPhase));
		}
		else {} // setActivePhase was called from onEnd of the previous phase, no need to do anything here.
		return true;
	}

	@JsonIgnore
	protected EventExecutor getEvents() {
		return CustomFacade.getGlobalEvents();
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
		
		if (action.isAllowed()) {
			action.perform();
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
		return new AIHandler() {
			@Override
			public void move(CardGame game) {
				throw new UnsupportedOperationException();
			}
			
			@Override
			public List<StackAction> getAvailableActions(Player player) {
				return new ArrayList<StackAction>();
			}
			
			@Override
			public StackAction click(Card card) {
				return new InvalidStackAction();
			}
		};
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

	public void onStart() {
	}
	
	protected boolean removeZone(CardZone zone) {
		return this.zones.remove(zone);
	}
}
