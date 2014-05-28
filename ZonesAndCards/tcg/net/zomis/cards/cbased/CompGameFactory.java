package net.zomis.cards.cbased;

import net.zomis.cards.components.ChosenCardComponent;
import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.ResourceMWKComponent;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.systems.CostAndEffectSystem;
import net.zomis.cards.systems.DeckFromEachCardSystem;
import net.zomis.cards.systems.DrawCardAtBeginningOfTurnSystem;
import net.zomis.cards.systems.DrawStartCards;
import net.zomis.cards.systems.GainMWKResourcesSystem;
import net.zomis.cards.systems.LimitedPlaysPerTurnSystem;
import net.zomis.cards.systems.MWKCardsSystem;
import net.zomis.cards.systems.PerformRPSSystem;
import net.zomis.cards.systems.PlayHandSystem;
import net.zomis.cards.systems.RPSCardsSystem;
import net.zomis.cards.systems.RecreateDeckSystem;
import net.zomis.cards.systems.RememberChosenCardSystem;
import net.zomis.cards.systems.SkipPlayerIfNoHealthSystem;

public class CompGameFactory {

	public static FirstCompGame rps() {
		FirstCompGame game = new FirstCompGame();
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		
		for (CompPlayer pl : game.getPlayers()) {
			pl.addComponent(new HandComponent(pl));
			pl.addComponent(new HealthComponent(50));
			pl.addComponent(new ChosenCardComponent());
			game.addPhase(new PlayerPhase(pl));
		}
		
		game.addSystem(new RPSCardsSystem(game));
		game.addSystem(new PlayHandSystem());
		game.addSystem(new CostAndEffectSystem());
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
			pl.addComponent(new HealthComponent(30));
			pl.addComponent(new ResourceMWKComponent(5, 3, 1));
			game.addPhase(new PlayerPhase(pl));
		}
		
		game.addSystem(new DrawCardAtBeginningOfTurnSystem());
		game.addSystem(new GainMWKResourcesSystem());
		game.addSystem(new RecreateDeckSystem());
//		game.addSystem(new RandomCardModelSystem());
		game.addSystem(new MWKCardsSystem());
		game.addSystem(new PlayHandSystem());
		game.addSystem(new CostAndEffectSystem());
		game.addSystem(new LimitedPlaysPerTurnSystem(1));
		// TODO: Buffered Health system(?) - don't decrease health directly, do it during cleanup. Listen for CleanupEvent and HealthModificationEvent
		
		game.addSystem(new DeckFromEachCardSystem(2, null));
		game.addSystem(new DrawStartCards(5));
		game.addSystem(new SkipPlayerIfNoHealthSystem());
		
//		game.addSystem(new DrawStartCards(game.getPlayers().get(0), 1));
		
		return game;
	}
	
}
