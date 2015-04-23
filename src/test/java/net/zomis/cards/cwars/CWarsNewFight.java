package net.zomis.cards.cwars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zomis.cards.ai.CGController;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_Better;
import net.zomis.cards.cwars2.ais.CWars2AI_InstantWin;
import net.zomis.cards.cwars2.ais.CWars2AI_NoWin;
import net.zomis.cards.cwars2.ais.CWars2AI_Random;
import net.zomis.cards.cwars2.ais.CWars2Decks;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.util.DeckList;
import net.zomis.custommap.ZomisSwing;
import net.zomis.fight.ext.ArenaParams;
import net.zomis.fight.ext.FNode;
import net.zomis.fight.ext.Fight;
import net.zomis.fight.ext.FightCollectors;
import net.zomis.fight.ext.FightIndexer;
import net.zomis.fight.ext.FightRes;
import net.zomis.fight.ext.GameFightNew;
import net.zomis.fight.ext.WinResult;

public class CWarsNewFight {

	public static void main(String[] args) {
		ZomisSwing.setup();
		List<CardAI> ais = new ArrayList<CardAI>(Arrays.asList(
			new CWars2AI_Better()
			, new CWars2AI_NoWin()
			, new CWars2AI_InstantWin()
			, new CWars2AI_Random()
		));
//		ais.forEach(ai -> ai.);
		
		GameFightNew<CardAI, CWars2Game> fight = new GameFightNew<>();
		
		Random random = new Random(4);
		Function<ArenaParams<CardAI>, CWars2Game> arenaCreator = params -> {
			CWars2Setup setup = CWars2Setup.newMultiplayerGame();
			CWars2Game game = setup.build();
			game.setRandom(random);
			DeckList deck = CWars2Decks.zomisMultiplayerDeck();
			setup.setDecks(deck.getCount(game), deck.getCount(game));
			game.startGame();
			return game;
		};
		
		FightIndexer<Fight<CardAI, CWars2Game>> indexer = new FightIndexer<>();
		ToIntFunction<FNode<Fight<CardAI, CWars2Game>>> winningIndex = f -> f.getF().getArena().determineWinner().getIndex();
		Function<FNode<Fight<CardAI, CWars2Game>>, CardAI> winningPlayer = f -> f.getF().getArenaParams().getPlayer(winningIndex.applyAsInt(f));
		Predicate<FNode<Fight<CardAI, CWars2Game>>> isWinner = f -> {
			if (!f.hasIndex(0))
				return true;
			return winningPlayer.apply(f) == f.getIndex(0);
		};
		Function<FNode<Fight<CardAI, CWars2Game>>, WinResult> fightRes = FightCollectors.fightResult(0, null, winningPlayer);
		
		indexer.addIndex("Player", FightCollectors.player1());
		indexer.addIndexPlus("Opponent", FightCollectors.player2(0));
		indexer.addDataAdvanced("Wins", Collectors.mapping(fightRes, FightCollectors.stats()));
		indexer.addDataAdvanced("WinnerCastle", FightCollectors.filteredCollector(isWinner, Collectors.averagingInt( 
				f -> f.getF().getArena().determineWinner().getResources().getResources(CWars2Res.CASTLE))));
		
		CWars2Game cm = null;
		System.out.println(cm);
		
		// TODO: "How many times did I play thief?"
		// TODO: "How many times did the winner play thief?"
		
		
		Stream<Fight<CardAI, CWars2Game>> stream = fight.createEvenFightStream(ais, 20, arenaCreator)
			.sequential()
			.peek(CWarsNewFight::autoplay)
			.peek(CWarsNewFight::printGame)
		;
		
		FightRes<Fight<CardAI, CWars2Game>> results = fight.processStream("Fights", stream, indexer);
		System.out.println(results.toStringBig());
	}
	
	public static void printGame(Fight<CardAI, CWars2Game> f) {
		Player pl1 = f.getArena().getPlayers().get(0);
		Player pl2 = f.getArena().getPlayers().get(1);
		
		System.out.println("Fight! " + f.getArenaParams().getPlayers() + " :::: " + pl1.getResources().getResources(CWars2Res.CASTLE) + 
				" vs. " + pl2.getResources().getResources(CWars2Res.CASTLE));
	}
	
	public static void autoplay(Fight<CardAI, CWars2Game> game) {
		CGController.finishWithAIs(game.getArena(), game.getArenaParams().getFirstPlayer(), game.getArenaParams().getSecondPlayer());
	}

	
}
