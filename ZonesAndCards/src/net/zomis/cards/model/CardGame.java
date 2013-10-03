package net.zomis.cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.zomis.ZomisUtils;
import net.zomis.cards.events.PhaseChangeEvent;
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
	private final List<Player> players = new LinkedList<>();
	
	private final List<GamePhase> phases = new ArrayList<>();
	/**
	 * Possible stack actions: Activate ability, play spell, play land, next phase, auto-triggered effects, +more
	 * Stack actions in Castle Wars: Play card, discard cards
	 */
	private final LinkedList<StackAction> stack = new LinkedList<>();
	
	public LinkedList<StackAction> getStack() {
		return stack;
	}
	
	@JsonManagedReference
	private final Set<CardZone> zones = new HashSet<>();
	private final Set<CardModel> availableCards = new HashSet<>();
	private int	activePhase = 0;
	
	public Set<CardModel> getAvailableCards() {
		return new TreeSet<>(availableCards);
	}
	
	public void addPlayer(Player player) {
		this.players.add(player);
		player.game = this;
	}

	public Set<CardZone> getZones() {
		return new TreeSet<>(zones);
	}
	
	public CardZone addZone(CardZone zone) {
		this.zones.add(zone);
		zone.game = this;
		return zone;
	}
	boolean removeZone(CardZone zone) {
		return this.zones.remove(zone);
	}

	public void addCard(CardModel card) {
		this.availableCards.add(card);
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}
	
	public List<GamePhase> getPhases() {
		return phases;
	}
	public void setActivePhase(GamePhase phase) {
		int index = phases.indexOf(phase);
		if (index == -1)
			throw new IllegalArgumentException();
		
		GamePhase oldPhase = getActivePhase();
		oldPhase.onEnd(this);
		
		this.activePhase = index;
		
		GamePhase newPhase = getActivePhase();
		newPhase.onStart(this);
		this.executeEvent(new PhaseChangeEvent(this, oldPhase, newPhase));
	}
	
	public GamePhase getActivePhase() {
		if (activePhase < 0 || activePhase > phases.size())
			return new GamePhase(); // return a "Null Object".
		return phases.get(activePhase);
	}

	public void addPhase(GamePhase phase) {
		this.phases.add(phase);
	}
	/**
	 * Get zone by name
	 * @param zoneName Name of Zone
	 * @return The zone with the matching name, or null if no zone was found
	 * @deprecated Working but not recommended to use. It is preferred to save a reference to the {@link CardZone} 
	 */
	@Deprecated
	public CardZone getZone(String string) {
		for (CardZone zone : this.zones) {
			if (zone.getName().equals(string))
				return zone;
		}
		return null;
	}

	public boolean nextPhase() {
		if (!this.isNextPhaseAllowed())
			return false;
		int nextPhase = (this.activePhase + 1) % this.phases.size();
		setActivePhase(this.phases.get(nextPhase));
		return true;
	}

	@JsonIgnore
	protected EventExecutor getEvents() {
		return CustomFacade.getGlobalEvents();
	}

	public StackAction processStack() {
		StackAction action = this.stack.pop();
		if (action == null) action = new StackAction();
		
		if (action.isAllowed()) {
			action.perform();
			executeEvent(new AfterActionEvent(this, action));
		}
		else CustomFacade.getLog().d("StackAction was not allowed: " + action);
		return action;
	}

	public void addStackAction(StackAction action) {
		this.stack.push(action);
	}
	public void addAndProcessStackAction(StackAction action) {
		this.addStackAction(action);
		this.processStack();
	}

	public void log() {
		ZomisUtils.echo("-----------------------------");
		ZomisUtils.echo(this);
		ZomisUtils.echo("Available cards: " + this.getAvailableCards());
		ZomisUtils.echo("Phases: " + this.getPhases());
		ZomisUtils.echo("Players: " + this.getPlayers());
		ZomisUtils.echo("Zones: " + this.getZones());
		ZomisUtils.echo("Active Phase: " + this.getActivePhase());
	}

	public AIHandler getAIHandler() {
		return null;
	}

	public Random getRandom() {
		return this.random;
	}
	protected void setRandomSeed(long seed) {
		this.random = new Random(seed);
	}

	public void registerListener(EventListener listener) {
		getEvents().registerListener(listener);
	}
	public void executeEvent(IEvent event) {
		getEvents().executeEvent(event);
	}
	public Player getCurrentPlayer() {
		GamePhase phase = getActivePhase();
		if (phase instanceof IPlayerPhase) {
			IPlayerPhase phase2 = (IPlayerPhase) this.getActivePhase();
			return phase2.getPlayer();
		}
		return null;
	}

	public boolean isNextPhaseAllowed() {
		return true;
	}
}
