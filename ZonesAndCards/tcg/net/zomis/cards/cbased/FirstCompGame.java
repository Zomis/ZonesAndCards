package net.zomis.cards.cbased;

import java.util.HashSet;
import java.util.Set;

import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.events2.GameStartedEvent;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.systems.DrawCardSystem;
import net.zomis.cards.systems.GameSystem;
import net.zomis.cards.systems.RandomCardModelSystem;
import net.zomis.cards.systems.RecreateDeckSystem;
import net.zomis.cards.util.DeckBuilder;
import net.zomis.cards.util.DeckList;
import net.zomis.events.EventExecutorGWT;
import net.zomis.events.IEvent;

public class FirstCompGame extends CardGame<CompPlayer, CompCardModel> {

	private final Set<GameSystem> systems = new HashSet<>();
	
	// TODO: card game with components
	// TODO: Feature-Request by @Mat'sMug: Support five players
	/**
	 * two players
	 * players have battlefields
	 * players have graveyard
	 * players have decks
	 * players have hands
	 * players have mana, get +1 total mana each turn (like Wartstone) 
	 * 
	 * event-based: When playing card, at the start of turn, when performing a fight, when cleaning up (state-based effects), etc.
	 * 
	 * component should only hold state
	 * card has a collection of components (list/set/map?)
	 * 
	 * Is it possible to not use generics once you have a proper component-based system? Using generics together with a Map<? extends Component, Component> would cause a mess.
	 */
	
	public FirstCompGame() {
		this.addPlayer(new CompPlayer());
		this.addPlayer(new CompPlayer());
		
		// TODO: Add components, systems, players and phases to FirstCompGame after constructor and before calling `startGame`
		for (CompPlayer pl : this.getPlayers()) {
			pl.addComponent(new HandComponent(pl));
			pl.addComponent(new DeckSourceComponent(pl));
			pl.addComponent(new DeckComponent(pl));
			this.addPhase(new PlayerPhase(pl));
		}
		
		this.addSystem(new DrawCardSystem());
		this.addSystem(new RecreateDeckSystem());
		this.addSystem(new RandomCardModelSystem());
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
		
		for (CompPlayer pl : getPlayers()) {
			if (!pl.hasComponent(DeckSourceComponent.class))
				return;
			// TODO: This code should not be here.
			DeckList deck = new DeckList("Deck").add(52, "Random Card");
			DeckBuilder.createExact(pl.getComponent(DeckSourceComponent.class), deck.getCount(this));
		}
		
	}
	
	public <T extends IEvent> T executeEvent(T event, Runnable runInBetween) {
		executeEvent(event, EventExecutorGWT.PRE);
		runInBetween.run();
		executeEvent(event, EventExecutorGWT.POST);
		return event;
	}
	
	public <T extends IEvent> T executeEvent(T event) {
		return getEvents().executeEvent(event);
	}
}
