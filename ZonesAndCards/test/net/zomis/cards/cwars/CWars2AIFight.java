package net.zomis.cards.cwars;

import net.zomis.cards.CardsTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_Better;
import net.zomis.cards.cwars2.ais.CWars2AI_InstantWin;
import net.zomis.cards.cwars2.ais.CWars2AI_Random;
import net.zomis.cards.cwars2.ais.CWars2Decks;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.ai.CardAI_Random;
import net.zomis.cards.util.DeckList;
import net.zomis.fight.FightInterface;
import net.zomis.fight.FightResults;
import net.zomis.fight.GameFight;

import org.junit.Ignore;
import org.junit.Test;

public class CWars2AIFight extends CardsTest<CWars2Game> {

	@Override
	protected void onBefore() {
		game = CWars2Setup.newMultiplayerGame().setDecks(CWars2Decks.zomisMultiplayerDeck(), CWars2Decks.zomisMultiplayerDeck()).start();
	}
	
	@Test
//	@Ignore
	public void deckSingleplayerFight() {
		GameFight<DeckList> fight = new GameFight<DeckList>("Singleplayer Deckfight");
		DeckList[] decks = new DeckList[]{ CWars2Decks.zomisSingleplayerBuilding(), CWars2Decks.zomisSingleplayerComputer(), CWars2Decks.zomisSingleplayerControl(),
//				CWars2Decks.zomisSingleplayerDefaultWithoutSpyAndSabotage98cards(), 
				CWars2Decks.zomisSingleplayerMyLocalDeck(),
				CWars2Decks.cheat(CWars2Decks.zomisSingleplayerComputer()) };
//		final CardAI ai = new CWars2AI_InstantWin();
		final CardAI ai = new CWars2AI_Better();
		
		FightResults<DeckList> results = fight.fightEvenly(decks, 100, new FightInterface<DeckList>() {
			@Override
			public DeckList determineWinner(DeckList[] players, int fightNumber) {
				game = CWars2Setup.newSingleplayerGame().withCard(CWars2Decks.cardCheat()).setDecks(players[0], players[1]).start();
				new CGController(game).setAIs(ai, ai).autoplay();
				return players[game.determineWinner().getIndex()];
			}
		});
		System.out.println(results.toStringMultiLine());
	}
	@Test
	@Ignore
	public void deckMultiplayerFight() {
		DeckList[] decks = new DeckList[]{ CWars2Decks.zomisMultiplayerDeck(), CWars2Decks.crapDeck(), CWars2Decks.zomisMultiplayerBuildingDeck() };
		final CardAI ai = new CWars2AI_InstantWin();
		GameFight<DeckList> fight = new GameFight<DeckList>("Multiplayer Deckfight with AI " + ai);
		
		FightResults<DeckList> results = fight.fightEvenly(decks, 100, new FightInterface<DeckList>() {
			@Override
			public DeckList determineWinner(DeckList[] players, int fightNumber) {
				CWars2Setup setup = CWars2Setup.newMultiplayerGame();
				game = setup.build();
				setup.setDecks(players[0].getCount(game), players[1].getCount(game));
				game.startGame();
				new CGController(game).setAIs(ai, ai).autoplay();
				return players[game.determineWinner().getIndex()];
			}
		});
		System.out.println(results.toStringMultiLine());
	}
	
	@Test
	public void finishAGame() {
		final CardAI ai = new CWars2AI_InstantWin();
		CWars2Game gme = CWars2Setup.newMultiplayerGame().setDecks(CWars2Decks.crapDeck(), CWars2Decks.crapDeck()).start();
		new CGController(gme).setAIs(ai, ai).autoplay();
	}
	
	@Test
	public void fightAll() {
		final DeckList deck1 = CWars2Decks.zomisSingleplayerMyLocalDeck();
		final DeckList deck2 = CWars2Decks.zomisSingleplayerMyLocalDeck();
		GameFight<CardAI> fight = new GameFight<CardAI>("AI Fight with decks " + deck1 + " and " + deck2);
		CardAI[] fighters = new CardAI[]{ new CWars2AI_InstantWin(), new CWars2AI_Better(), new CWars2AI_Random() };
		
		FightResults<CardAI> results = fight.fightEvenly(fighters, 100, new FightInterface<CardAI>() {
			@Override
			public CardAI determineWinner(CardAI[] players, int fightNumber) {
				game = CWars2Setup.newSingleplayerGame()
						.setDecks(deck1, deck2)
						.start();
				CGController.finishWithAIs(game, players[0], players[1]);
				return players[game.determineWinner().getIndex()];
			}
		});
		System.out.println(results.toStringMultiLine());
	}
			
	@Test
	public void bestOpponentForDeck() {
		final DeckList myDeck = CWars2Decks.zomisSingleplayerMyLocalDeck();
		final DeckList testDeck = CWars2Decks.zomisSingleplayerControl();
		final CardAI myAI = new CWars2AI_Better();
		
		// TODO: Why is this method not using the GameFight class to create FightResults? Perhaps because one of the players is set to null below...
		
		FightResults<CardAI> results = new FightResults<CardAI>("Best opponent for deck " + myDeck.getName(), false);
		CardAI[] fighters = new CardAI[]{ new CWars2AI_InstantWin(), new CWars2AI_Better(), new CWars2AI_Random() };
		FightInterface<CardAI> fightInterface = new FightInterface<CardAI>() {
			@Override
			public CardAI determineWinner(CardAI[] players, int fightNumber) {
				game = CWars2Setup.newSingleplayerGame()
					.setDecks(myDeck, testDeck)
					.start();
				CGController.finishWithAIs(game, myAI, players[1]);
				return players[game.determineWinner().getIndex()];
			}
		};
		for (CardAI opponent : fighters) {
			CardAI[] players = new CardAI[]{ null, opponent };
			for (int i = 1; i <= 100; i++) {
				results.saveResult(players, fightInterface.determineWinner(players, i));
			}
		}
		
		System.out.println(results.toStringMultiLine());
	}
			
	@Test
	public void fight() {
		
		GameFight<CardAI> fight = new GameFight<CardAI>("Simple fight with two AIs");
		CardAI[] decks = new CardAI[]{ new CWars2AI_InstantWin(), new CardAI_Random() };
		
		FightResults<CardAI> results = fight.fightRandom(decks, 10, new FightInterface<CardAI>() {
			@Override
			public CardAI determineWinner(CardAI[] players, int fightNumber) {
				game = CWars2Setup.newMultiplayerGame()
					.setDecks(CWars2Decks.zomisMultiplayerDeck(), CWars2Decks.zomisMultiplayerDeck())
					.start();
				CGController.finishWithAIs(game, players[0], players[1]);
				return players[game.determineWinner().getIndex()];
			}
		});
		System.out.println(results.toStringMultiLine());
	}

}
