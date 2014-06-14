package net.zomis.cards.cr;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.zomis.cards.CardsTest;
import net.zomis.cards.ai.CGController;
import net.zomis.cards.crgame.CRCard;
import net.zomis.cards.crgame.CRCardGame;
import net.zomis.cards.crgame.CRCardModel;
import net.zomis.cards.crgame.CRCardType;
import net.zomis.cards.crgame.CRCards;
import net.zomis.cards.crgame.CRPlayer;
import net.zomis.cards.crgame.CRRes;
import net.zomis.cards.crgame.ais.AI2;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;

import org.junit.Test;

public class CRTest extends CardsTest<CRCardGame> {

	@Override
	protected void onBefore() {
		game = new CRCardGame();
		game.setRandomSeed(42);
		game.startGame();
	}
	
	@Test
	public void aiTest() {
		CGController controller = new CGController(game);
		controller.setAI(0, new AI2());
		controller.setAI(1, new AI2());
		StackAction act;
		do {
			if (game.isTargetSelectionMode()) {
				System.out.println("Expecting failure because of target selection."); // TODO: Make CRTest work with target selection.
			}
			act = controller.play();
			System.out.println("------" + act + " status " + game.getFirstPlayer().getResources() + " vs. " + game.getFirstPlayer().getNextPlayer().getResources());
			if (game.isGameOver())
				break;
		}
		while (act.actionIsPerformed());
	}
	
	@Test
	public void logStats() {
		game = new CRCardGame();
		Collection<CRCardModel> cards = game.getCards().values();
		Map<CRCardType, List<CRCardModel>> typeCards = cards.stream().collect(groupingBy(CRCardModel::getType));
		typeCards.forEach((key, value) -> System.out.println("Key: " + key + " size " + value.size()));
		long sum = typeCards.values().stream().collect(Collectors.summarizingInt(List::size)).getSum();
		System.out.println("Total cards: " + sum);
		
		game.getPlayers().forEach(this::outputPlayerInfo);
		
		assertEquals(game.getPlayers().get(0).getTotalCards(), game.getPlayers().get(1).getTotalCards());
	}
	
	public void outputPlayerInfo(CRPlayer player) {
		System.out.println(player.getName() + ": " + player.getTotalCards());
	}
	
	@Test
	public void bearTrapFailed() {
		superPlayCard(CRCards.MONKING);
		assertZoneSize(1, game.getCurrentPlayer().getUserZone());
		nextTurn();
		nextTurn();
		nextTurn();
		nextTurn();

		nextTurn();
		nextTurn();
		nextTurn();
		nextTurn();
		
		nextTurn();
		superPlayCard(CRCards.EXPLODING_BEAR_TRAP);
		nextTurn();
		assertZoneSize(1, game.getCurrentPlayer().getUserZone());
	}
	
	@Test
	public void bearTrap() {
		superPlayCard(CRCards.MONKING);
		assertZoneSize(1, game.getCurrentPlayer().getUserZone());
		nextTurn();
		superPlayCard(CRCards.EXPLODING_BEAR_TRAP);
		nextTurn();
		assertZoneSize(0, game.getCurrentPlayer().getUserZone());
	}
	
	@Test
	public void pimpQuestion() {
		int siteQuality = game.getCurrentPlayer().getQuality();
		superPlayCard(CRCards.YOU_ABOUT_YOU);
		nextTurn();
		nextTurn();
		assertEquals(siteQuality + CRCards.YOU_ABOUT_YOU. get(CRRes.QUALITY), game.getCurrentPlayer().getQuality());
		siteQuality = game.getCurrentPlayer().getQuality();
		superPlayCard(CRCards.PIMP_QUESTION);
		target(game.getCurrentPlayer().getZombieZone().getTopCard());
		nextTurn();
		nextTurn();
		assertEquals(siteQuality + CRCards.YOU_ABOUT_YOU.get(CRRes.QUALITY) + 2, game.getCurrentPlayer().getQuality());
	}
	
	private void target(CRCard card) {
		assertTrue("Target selection mode is not active", game.isTargetSelectionMode());
		assertTrue(game.click(card));
		assertFalse(game.isTargetSelectionMode());
	}
	
	@Test
	public void user() {
		assertResources(game.getCurrentPlayer().getResources(), CRRes.HOURS_AVAILABLE, 2);
		superPlayCard(CRCards.MONKING.getName());
		assertZoneSize(1, game.getCurrentPlayer().getUserZone());
		assertEquals(CRCards.USER, game.getCurrentPlayer().getUserZone().getTopCard().getModel());
		nextTurn();
		nextTurn();
		assertResources(game.getCurrentPlayer().getResources(), CRRes.HOURS_AVAILABLE, 3);
	}
	
	@Test
	public void ask() {
		assertResources(game.getCurrentPlayer().getResources(), CRRes.HOURS_AVAILABLE, 2);
		int res = game.getCurrentPlayer().getResources().getResources(CRRes.QUALITY);
		superPlayCard(CRCards.ASK_A_QUESTION.getName());
		assertZoneSize(1, game.getCurrentPlayer().getZombieZone());
		assertEquals(CRCards.YOU_ABOUT_YOU, game.getCurrentPlayer().getZombieZone().getTopCard().getModel());
		nextTurn();
		nextTurn();
		assertEquals(res + 5, game.getCurrentPlayer().getResources().getResources(CRRes.QUALITY));
	}
	
	private CRCard superPlayCard(CardModel card) {
		return superPlayCard(card.getName());
	}
	
	private CRCard superPlayCard(String name) {
		game.getCurrentPlayer().getHand().getBottomCard().zoneMoveOnBottom(null);
		CRCardModel cardModel = game.getCardModel(name);
		assertNotNull("Card Model not found: " + name, cardModel);
		CRCard card = game.getCurrentPlayer().getHand().createCardOnTop(cardModel);
		assertEquals(game.getCurrentPlayer().getHand(), card.getCurrentZone());
		assertNotNull(card.getPlayer());
		playCard(name);
		return card;
	}

	private CRCard playCard(String cardName) {
		assertEquals("Stack not empty", 0, game.stackSize());
//		assertFalse("Cannot play card while in target selection mode", game.isTargetSelectionMode());
		CRCard card = cardToHand(cardName, game.getCurrentPlayer());
		assertTrue(game.click(card));
		assertEquals(0, game.stackSize());
		assertFalse("Card was not removed from hand: " + cardName, card.getCurrentZone() == game.getCurrentPlayer().getHand());
//		assertEquals("Card was not removed from hand: " + cardName, cardsOnHand - 1, game.getCurrentPlayer().getHand().size());
		return card;
	}
	
	private CRCard cardToHand(final String cardName, final CRPlayer player) {
		CRCardModel model = game.getCards().get(cardName);
//		if (model == null)
//			throw new NullPointerException("Card not found in game: " + cardName);
		assertNotNull("Card not found in game: " + cardName);
		Predicate<CRCard> isCard = c -> c.getModel().equals(model);
		CardZone<CRCard> search = player.getHand();
		assertFalse("No cards in hand", search.isEmpty());
		Optional<CRCard> card = search.cardList().stream().filter(isCard).findAny();
		if (!card.isPresent()) {
			search = game.getCurrentPlayer().getDeck();
			assertFalse(search.isEmpty());
			card = search.cardList().stream().filter(isCard).findAny();
			player.getHand().getTopCard().zoneMoveOnBottom(search);
			assertTrue("Card exists in game but was not added to deck: " + cardName, card.isPresent());
			card.get().zoneMoveOnBottom(player.getHand());
		}
		assertTrue("Card not found: " + cardName, card.isPresent());
		
		return card.get();
	}

	private void nextTurn() {
		assertEquals(0, game.stackSize());
		assertTrue(game.nextPhase());
	}
	
}
