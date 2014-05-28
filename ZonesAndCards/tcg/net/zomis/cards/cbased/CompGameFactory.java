package net.zomis.cards.cbased;

import net.zomis.cards.components.DeckComponent;
import net.zomis.cards.components.DeckSourceComponent;
import net.zomis.cards.components.HandComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.systems.DrawCardAtBeginningOfTurnSystem;
import net.zomis.cards.systems.DrawStartCards;
import net.zomis.cards.systems.LimitedPlaysPerTurnSystem;
import net.zomis.cards.systems.PlayHandSystem;
import net.zomis.cards.systems.RandomCardModelSystem;
import net.zomis.cards.systems.RecreateDeckSystem;
import net.zomis.cards.systems.SimpleDeckSystem;
import net.zomis.cards.systems.SkipPlayerIfNoHealthSystem;

public class CompGameFactory {

	public static FirstCompGame simple() {
		FirstCompGame game = new FirstCompGame();
		
		game.addPlayer(new CompPlayer());
		game.addPlayer(new CompPlayer());
		
		for (CompPlayer pl : game.getPlayers()) {
			pl.addComponent(new HandComponent(pl));
			pl.addComponent(new DeckSourceComponent(pl));
			pl.addComponent(new DeckComponent(pl));
			pl.addComponent(new HealthComponent(30));
			game.addPhase(new PlayerPhase(pl));
		}
		
		game.addSystem(new DrawCardAtBeginningOfTurnSystem());
		game.addSystem(new RecreateDeckSystem());
		game.addSystem(new RandomCardModelSystem());
		game.addSystem(new PlayHandSystem());
		game.addSystem(new LimitedPlaysPerTurnSystem(1));
		// TODO: Buffered Health system(?) - don't decrease health directly, do it during cleanup. Listen for CleanupEvent and HealthModificationEvent
		
		game.addSystem(new SimpleDeckSystem());
		game.addSystem(new DrawStartCards(5));
		game.addSystem(new SkipPlayerIfNoHealthSystem());
//		game.addSystem(new DrawStartCards(game.getPlayers().get(0), 1));
		
		return game;
	}
	
}
