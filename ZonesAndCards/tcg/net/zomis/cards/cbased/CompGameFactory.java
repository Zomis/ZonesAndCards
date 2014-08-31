package net.zomis.cards.cbased;

import net.zomis.cards.components.BattlefieldComponent;
import net.zomis.cards.components.ChosenCardComponent;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.ManaComponent;
import net.zomis.cards.components.PlayerCardComponent;
import net.zomis.cards.components.ResourceMWKComponent;
import net.zomis.cards.components.SpecialZoneComponent;
import net.zomis.cards.model.GamePhase;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.sets.HSCompCards;
import net.zomis.cards.sets.MWKCardsSystem;
import net.zomis.cards.sets.RPSCardsSystem;
import net.zomis.cards.systems.AttackWithBattlefieldSystem;
import net.zomis.cards.systems.ConsumeCardSystem;
import net.zomis.cards.systems.CreateDeckOnceFromSourceSystem;
import net.zomis.cards.systems.DamageIncreasingWhenOutOfCardsSystem;
import net.zomis.cards.systems.DeckFromEachCardSystem;
import net.zomis.cards.systems.DrawCardAtBeginningOfTurnSystem;
import net.zomis.cards.systems.DrawStartCards;
import net.zomis.cards.systems.GainMWKResourcesSystem;
import net.zomis.cards.systems.GameOverIfNoHealth;
import net.zomis.cards.systems.IncreaseManaSystem;
import net.zomis.cards.systems.LimitedHandSizeSystem;
import net.zomis.cards.systems.LimitedPlaysPerTurnSystem;
import net.zomis.cards.systems.PerformRPSSystem;
import net.zomis.cards.systems.PlayHandCostEffectSystem;
import net.zomis.cards.systems.RecreateDeckSystem;
import net.zomis.cards.systems.RememberChosenCardSystem;
import net.zomis.cards.systems.RestoreManaOnTurnStartSystem;
import net.zomis.cards.systems.SkipPlayerIfNoHealthSystem;

public class CompGameFactory {
	// TODO: Possible HS extensions: Minions share attack and health, Lifelink (heal either self or player, or random filtered character)
	/*
	 * x players
	 * event-based: When playing card, at the start of turn, when performing a fight, when cleaning up (state-based effects), etc.
	 * 
	 * components only hold state
	 * each card (and CardModel?) has a collection of components
	 * 
	 * How can generics be used with the component-based system? Using generics together with a Map<? extends Component, Component> would cause a mess.
	 * */

	public static FirstCompGame rps() {
		FirstCompGame game = new FirstCompGame();
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		
		for (CompPlayer pl : game.getPlayers()) {
			pl.addComponent(new HandComponent(pl));
			pl.addComponent(new HealthComponent(pl.getResources(), 50));
			pl.addComponent(new ChosenCardComponent());
			game.addPhase(new GamePhase(pl));
		}
		
		game.addCards(new RPSCardsSystem());
		
		game.addSystem(new PlayHandCostEffectSystem());
		game.addSystem(new LimitedPlaysPerTurnSystem(1));
		game.addSystem(new RememberChosenCardSystem());
		game.addSystem(new PerformRPSSystem());
		
		game.addSystem(new RecreateDeckSystem());
		game.addSystem(new DeckFromEachCardSystem(1, HandComponent.class));
//		game.addSystem(new SkipPlayerIfNoHealthSystem());

		return game;
	}
	
	public static FirstCompGame simple() {
		FirstCompGame game = new FirstCompGame();
		
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		
		for (CompPlayer pl : game.getPlayers()) {
			pl.addComponent(new HandComponent(pl));
			pl.addComponent(new DeckSourceComponent(pl));
			pl.addComponent(new DeckComponent(pl));
			pl.addComponent(new HealthComponent(pl.getResources(), 30));
			pl.addComponent(new ResourceMWKComponent(pl.getResources(), 5, 3, 1));
			game.addPhase(new GamePhase(pl));
		}
		
		game.addCards(new MWKCardsSystem());
		
		game.addSystem(new DrawCardAtBeginningOfTurnSystem());
		game.addSystem(new GainMWKResourcesSystem());
		game.addSystem(new RecreateDeckSystem());
		game.addSystem(new PlayHandCostEffectSystem());
		game.addSystem(new ConsumeCardSystem());
		game.addSystem(new LimitedPlaysPerTurnSystem(2));
		
		game.addSystem(new DeckFromEachCardSystem(2, null));
		game.addSystem(new DrawStartCards(5));
		game.addSystem(new SkipPlayerIfNoHealthSystem());
		game.addAction(new CompCardModel("End Turn"), () -> new NextTurnAction(game) );
		
//		game.addSystem(new DrawStartCards(game.getPlayers().get(0), 1));
		
		return game;
	}

	public static FirstCompGame hearthstone() {
		FirstCompGame game = new FirstCompGame();
		
		game.addPlayer(new CompPlayer().setName("Player1"));
		game.addPlayer(new CompPlayer().setName("Player2"));
		
		for (CompPlayer pl : game.getPlayers()) {
			pl.addComponent(new SpecialZoneComponent(pl));
			pl.addComponent(new PlayerCardComponent(pl));
			
			pl.addComponent(new HandComponent(pl));
			pl.addComponent(new DeckSourceComponent(pl));
			pl.addComponent(new DeckComponent(pl));
			pl.addComponent(new BattlefieldComponent(pl));
			
			HealthComponent health = new HealthComponent(pl.getResources(), 30);
			pl.getRequiredComponent(PlayerCardComponent.class).getCard().addComponent(health);
			pl.addComponent(health);
			pl.addComponent(new ManaComponent(pl.getResources(), 0));
			game.addPhase(new GamePhase(pl));
		}
		
		game.addCards(new HSCompCards());
		
		// Mana
		game.addSystem(new IncreaseManaSystem(1, 10)); // increase mana first, restore mana later
		game.addSystem(new RestoreManaOnTurnStartSystem());
		// TODO: ManaOverloadSystem -- Uses an `OverloadComponent` for both cards and players. Checks for turn start and afterCardPlayed
		
		// Draw cards
		game.addSystem(new DrawCardAtBeginningOfTurnSystem());
		game.addSystem(new DamageIncreasingWhenOutOfCardsSystem());
		game.addSystem(new LimitedHandSizeSystem(10, card -> card.destroy()));
		
		// Play cards
		game.addSystem(new AttackWithBattlefieldSystem());
		game.addSystem(new PlayHandCostEffectSystem());
//		game.addSystem(new ConsumeCardSystem());
		
		// Use cards on battlefield
		
		// Initial setup
		game.addSystem(new DeckFromEachCardSystem(2, null));
		game.addSystem(new CreateDeckOnceFromSourceSystem());
		game.addSystem(new DrawStartCards(3));
		game.addSystem(new DrawStartCards(game.getPlayers().get(1), 1));
		// TODO: game.addSystem(new GiveStartCard(game.getPlayers().get(1), "The Coin"));
		
		// General setup
		game.addAction(new CompCardModel("End Turn"), () -> new NextTurnAction(game) );
		game.addSystem(new GameOverIfNoHealth());
		
		return game;
	}
	
}
