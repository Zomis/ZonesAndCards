package test.net.zomis.cards;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import net.zomis.ZomisList;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.hearts.HeartsGame;
import net.zomis.cards.hearts.HeartsSuperGame;
import net.zomis.cards.hearts.SimpleHeartsAI;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;

import org.junit.Test;

public class HeartsSuperTest extends CardsTest<HeartsSuperGame> {

	@Override
	protected void onBefore() {
		game = new HeartsSuperGame(new String[]{ "BUBU", "Minken", "Tejpbit", "Zomis" });
		game.setRandomSeed(42);
	}
	
	@Test
	public void f() {
		Assert.assertEquals(ClassicCard.RANK_2, game.getAceConfig().getMinRank());
		Assert.assertEquals(ClassicCard.RANK_ACE_HIGH, game.getAceConfig().getMaxRank());
		
		SimpleHeartsAI ai = new SimpleHeartsAI(game.getRandom());
		for (Player pl : game.getPlayers()) {
			pl.setAI(ai);
		}
		game.startGame();
		for (Player pl : game.getPlayers()) {
			CardPlayer player = (CardPlayer) pl;
			Assert.assertEquals(HeartsGame.MAGIC_NUMBER, player.getHand().size());
			ClassicCard randomCard = (ClassicCard) ZomisList.getRandom(player.getHand().cardList()).getModel();
			Assert.assertNotSame(randomCard.getSuite().isBlack(), randomCard.getSuite().isRed());
		}
		
		
		int i = 0;
		while (!game.isGameOver() && i < 4000) {
			StackAction sa = null;
			if (game.getCurrentPlayer() == null) {
				for (Player pl : game.getPlayers()) {
					sa = game.callPlayerAI(pl);
					Assert.assertTrue(i + pl.toString() + " tried to do illegal action: " + sa.toString(), sa.actionIsPerformed() || game.getCurrentPlayer() != null);
//					CustomFacade.getLog().i("Action By Player: " + pl + sa);
					if (game.getCurrentPlayer() != null)
						break;
				}
			}
			else {
				CardPlayer pl = game.getCurrentPlayer();
				sa = game.callPlayerAI();
				Assert.assertTrue("Current player is " + game.getCurrentPlayer() + " old is " + pl + " pile is " + game.getPile(), game.getCurrentPlayer() != pl || game.getPile().isEmpty());
				Assert.assertTrue(i + sa.toString(), sa.actionIsPerformed());
//				CustomFacade.getLog().i("Action: " + pl + sa);
			}
			if (game.getPile().isEmpty()) verifyHands();
			++i;
		}
		
		Assert.assertTrue(i + " -- " + Arrays.toString(game.getScores()), game.isGameOver());
		CustomFacade.getLog().i("Stopped after i " + i + Arrays.toString(game.getScores()));
	}

	private Set<CardModel> cards = new HashSet<CardModel>();
	private void verifyHands() {
		cards.clear();
		int i = -1;
		for (Player pl : game.getPlayers()) {
			CardPlayer player = (CardPlayer) pl;
			if (i == -1)
				i = player.getHand().size();
			Assert.assertEquals(player.toString(), i, player.getHand().size());
			for (Card card : player.getHand().cardList()) {
				Assert.assertTrue(cards.add(card.getModel()));
			}
		}
	}

	
	
}
