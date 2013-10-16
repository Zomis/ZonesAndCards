package net.zomis.cards.simple;

import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.GamePhase;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.simple.cardeffects.CostDecrease;
import net.zomis.cards.simple.cardeffects.LifeChange;
import net.zomis.cards.simple.cardeffects.OpponentLifeChange;
import net.zomis.cards.simple.cardeffects.SimpleStackAction;

public class SimpleGame extends CardGame {
	
	private final CardZone playedCards;
	
	public CardZone getPlayedCards() {
		return playedCards;
	}
	
	public SimpleGame() {
		super();
		
		this.addPlayer(new SimplePlayer().setName("Zomis"));
		this.addPlayer(new SimplePlayer().setName("BUBU"));
		
		// TEST: Don't use ZoneChange listeners, use StackActions instead!
		this.addCard(new SimpleCard("Life2").setCost(1).setAction(new CostDecrease(new LifeChange(2))));
		this.addCard(new SimpleCard("Damage3").setCost(1).setAction(new CostDecrease(new OpponentLifeChange(-3))));
		this.addCard(new SimpleCard("AddResourcePerTurn1").setCost(4).setAction(new CostDecrease(new SimpleStackAction() {
			@Override
			public void perform(SimpleCard cardModel, Card card, SimplePlayer byPlayer) {
				int rpt = byPlayer.getResourcesPerTurn();
				byPlayer.setResourcesPerTurn(rpt + 1);
			}
		})));
		
		playedCards = new CardZone("PlayedCards");
		this.addZone(playedCards).setGloballyKnown(true);
		
		GamePhase firstPhase = null;
		
		for (Player pl : this.getPlayers()) {
			SimplePlayer pl2 = (SimplePlayer) pl;
			GamePhase upkeep = new UpkeepPhase(pl);
			this.addPhase(upkeep);
			
			GamePhase main = new PlayerPhase(pl);
			this.addPhase(main);
			if (firstPhase == null)
				firstPhase = main;

			CardZone library = new CardZone("Library-"+pl.getName());
			pl2.library = library;
			for (CardModel card : this.getAvailableCards()) {
				for (int i = 0; i < 4; i++)
					library.createCardOnTop(card);
			}
			this.addZone(library);
			library.shuffle();
			
			CardZone hand = new CardZone("Hand-"+pl.getName());
			for (int i = 0; i < 5; i++)
				hand.cardList().add(library.cardList().removeFirst());
			this.addZone(hand);
			
			pl2.hand = hand;
			hand.setKnown(pl, true);
		}
		this.setActivePhase(firstPhase);
	}

	@Override
	public AIHandler getAIHandler() {
		return new SimpleAIHandler();
	}
	
		
}
