package net.zomis.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.PostScorer;
import net.zomis.aiscores.PreScorer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreSet;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.cwars2.CWars2DiscardAction;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2PlayAction;
import net.zomis.cards.cwars2.CWars2Setup;
import net.zomis.cards.cwars2.ais.CWars2AI_Random;
import net.zomis.cards.cwars2.ais.CWars2CardCost;
import net.zomis.cards.cwars2.ais.CWars2Decks;
import net.zomis.cards.cwars2.ais.CWars2OpponentNeeds;
import net.zomis.cards.cwars2.ais.CWars2ScorerNeeds;
import net.zomis.cards.cwars2.ais.CWars2ScorerResourceDemand;
import net.zomis.cards.cwars2.ais.CanWinScorer;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.ai.IsActionClass;
import net.zomis.custommap.CustomFacade;
import net.zomis.custommap.view.Log4jLog;
import net.zomis.custommap.view.ZomisLog;
import net.zomis.custommap.view.swing.ZomisSwingLog4j;
import net.zomis.fight.FightInterface;
import net.zomis.fight.FightResults;
import net.zomis.fight.GameFight;
import net.zomis.fight.PlayerResults;
import net.zomis.utils.ZomisList;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CWarsGeneticsTest implements FightInterface<CardAI> {
/**
 * Match the best deck with the best AI, and match the best AI with the best deck. 2-way improvement
 * 
 */
	@BeforeClass
	public static void beforeClass() {
		if (!CustomFacade.isInitialized()) {
			ZomisSwingLog4j.addConsoleAppender(Log4jLog.DEFAULT_LAYOUT);
			new CustomFacade(new Log4jLog("Cards"));
		}
	}
	
	@Test
	@Ignore
	public void fighty() {
		List<CardAI> configs = new ArrayList<CardAI>();
		configs.add(new CWars2AI_Random());
		while (configs.size() < 6)
			configs.add(start2());
		
		configs = iterationFight(configs, 20);
		output(configs);
		configs = iterationFight(configs, 50);
		output(configs);
		
	}
	
	private void output(List<CardAI> configs) {
		for (CardAI ai : configs) {
			ScoreConfig<Player, Card<?>> conf = ai.getConfig();
			ZomisLog.info(conf);
		}
	}

	private List<CardAI> iterationFight(List<CardAI> ais, int fights) {
		// Perform the fights
		GameFight<CardAI> fight = new GameFight<CardAI>("Iteration fight " + fights + " times");
		FightResults<CardAI> result = fight.fightEvenly(ais.toArray(new CardAI[0]), fights, this);
		
		// Output the fight results
		ZomisLog.info(result.toStringMultiLine());
		List<PlayerResults<CardAI>> list = result.getResultsAsc();
		Collections.reverse(list);
		for (PlayerResults<CardAI> ee : list)
			ZomisLog.info(ee.getPlayer() + " " + ee.calculatePercentage());
		
		// Split into qualified and unqualified
		int splitIndex = list.size() / 2;
		List<CardAI> qualified = list.subList(0, splitIndex).stream().map(pres -> pres.getPlayer()).collect(Collectors.toList());
		
		// Make modifications of the qualified AIs
		for (CardAI ai : new ArrayList<CardAI>(qualified)) {
			qualified.add(modify(ai));
		}
		return qualified;
	}
	
	
	private CardAI modify(CardAI ai) {
		ScoreConfig<Player, Card<?>> config = ai.getConfig();
		ScoreSet<Player, Card<?>> scores = config.getScorers();
		List<FScorer<Player, Card<?>>> scorerList = new ArrayList<FScorer<Player, Card<?>>>(scores.keySet());
		FScorer<Player, Card<?>> rand = ZomisList.getRandom(scorerList);
		
		ScoreConfigFactory<Player, Card<?>> factory = new ScoreConfigFactory<Player, Card<?>>();
		for (PreScorer<Player> ee : config.getPreScorers())
			factory.withPreScorer(ee);
		for (PostScorer<Player, Card<?>> ee : config.getPostScorers())
			factory.withPost(ee);
		
		for (Entry<FScorer<Player, Card<?>>, Double> ee : scores.entrySet()) {
			if (ee.getKey() == rand)
				factory.withScorer(ee.getKey(), (random.nextDouble() - 0.5) * 2);
			else factory.withScorer(ee.getKey(), ee.getValue());
		}
		
		CardAI newAI = new CardAI();
		newAI.setConfig(factory);
		
		return newAI;
	}


	private final Random random = new Random();
	
	private CardAI start2() {
		CardAI ai = new CardAI();
		
		ai.setConfig(start());
		assertEquals(7, ai.getConfig().getScorers().size());
		assertEquals(7, ai.getConfig().getScorers().entrySet().size());
		return ai;
	}
	private ScoreConfig<Player, Card<?>> start() {
		ScoreConfigFactory<Player, Card<?>> config = new ScoreConfigFactory<Player, Card<?>>();
		config.withPreScorer(new CWars2ScorerResourceDemand());
		config.withScorer(new CWars2CardCost(), startWeight());
		config.withScorer(new CWars2ScorerNeeds(), startWeight());
		config.withScorer(new CWars2OpponentNeeds(), startWeight()); // What is bad for my opponent is good for me
		
		config.withScorer(new CanWinScorer(), startWeight()); // If it's possible to instantly win, then it's a really good idea.
		config.withScorer(new IsActionClass(CWars2PlayAction.class), startWeight());
		config.withScorer(new IsActionClass(CWars2DiscardAction.class), startWeight());
		config.withScorer(new IsActionClass(NextTurnAction.class), startWeight());
		config.multiplyAll(0);
		return config.build();
	}
	
	
	private double startWeight() {
		return (random.nextDouble() - 0.5) * 2;
	}

	@Override
	public CardAI determineWinner(CardAI[] players, int fightNumber) {
		CWars2Setup setup = CWars2Setup.newMultiplayerGame();
		setup.setDecks(CWars2Decks.zomisMultiplayerDeck().getCount(setup.build()), CWars2Decks.zomisMultiplayerDeck().getCount(setup.build()));
		int playerAindex = random.nextInt(2);
		CWars2Game game = setup.build();
		game.startGame();

		CGController controller = new CGController(game);
		controller.setAI(playerAindex, players[0]);
		controller.setAI(1 - playerAindex, players[1]);
		controller.autoplay();
		return (CardAI) controller.getAI(game.determineWinner());
	}
	
}
