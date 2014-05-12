package net.zomis.cards.poker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.zomis.cards.classics.AceValue;
import net.zomis.cards.classics.CardPlayer;
import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.classics.ClassicGame;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.ZoneMoveAction;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.poker.validation.PokerHandEval;
import net.zomis.cards.poker.validation.PokerHandResult;
import net.zomis.custommap.CustomFacade;

public class PokerGame extends ClassicGame {

	private final ClassicCardZone	deck;

	public PokerGame() {
		super(AceValue.HIGH);
		this.setActionHandler(new ActionHandler() {
//			@Override
//			public <E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(E cardGame, Player player) {
//				List<StackAction> list = new ArrayList<StackAction>();
//				CardPlayer pl = (CardPlayer) player;
//				list.add(new RedrawAction(pl, 7));
//				list.add(new DrawCardAction(pl));
//				for (Card<ClassicCard> card : pl.getHand().cardList()) {
////					card.getModel();
//					list.add(new DiscardCardAction(card));
//				}
//				return list;
//			} // TODO: Fixa PokerGame ActionHandler
			
			@Override
			public StackAction click(Card<?> card) {
				return null;
			}

			@Override
			public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
				List<Card<?>> cards = new ArrayList<Card<?>>();
				
				
				return cards;
			}
		});
		
		deck = new ClassicCardZone("Deck");
		deck.addDeck(this, 2);
		CardPlayer pl = new CardPlayer();
		pl.setName("BUBU");
		pl.setHand(new ClassicCardZone("Hand"));
		addPlayer(pl);
		addZone(deck);
		deck.setGloballyKnown(true);
		pl.getHand().setGloballyKnown(true);
		addZone(pl.getHand());
		this.addPhase(new PlayerPhase(pl));
	}
	@Override
	protected void onStart() {
		deck.shuffle(this.getRandom());
	}
	
	private static class RedrawAction extends StackAction {
		private final CardPlayer	player;
		private final int	size;

		public RedrawAction(CardPlayer pl, int size) {
			this.player = pl;
			this.size = size;
		}
		
		@Override
		protected void onPerform() {
			PokerGame game = (PokerGame) player.getGame();
			player.getHand().moveToBottomOf(game.deck);
			game.deck.shuffle();
			for (int i = 0; i < this.size - 1; i++)
				game.deck.getTopCard().zoneMoveOnTop(player.getHand());
			game.addAndProcessStackAction(new DrawCardAction(player));
		}
		
	}
	
	public static class DiscardCardAction extends ZoneMoveAction {
		public DiscardCardAction(Card<ClassicCard> card) {
			super(card);
			this.setDestination(((PokerGame) card.getGame()).deck);
			this.setSendToBottom();
		}
		@Override
		public String toString() {
			return "Discard " + this.getCard();
		}
	}
	
	public static class DrawCardAction extends ZoneMoveAction {
		private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();
		
		public DrawCardAction(CardPlayer player) {
			super(((PokerGame) player.getGame()).deck.getTopCard());
			this.setDestination(player.getHand());
		}
		
		@Override
		protected void onPerform() {
			super.onPerform();
			// Calculate Hand
			@SuppressWarnings("unchecked")
			CardZone<Card<ClassicCard>> dest = (CardZone<Card<ClassicCard>>) getDestination();
			LinkedList<Card<ClassicCard>> list = dest.cardList();
			ClassicCard[] cards = new ClassicCard[list.size()];
			ListIterator<Card<ClassicCard>> it = list.listIterator();
			while (it.hasNext()) {
				int index = it.nextIndex();
				cards[index] = (ClassicCard) it.next().getModel();
			}
			PokerHandResult result = eval.evaluate(cards);
			CustomFacade.getLog().i("Card Result: " + result);
		}
		
		@Override
		public String toString() {
			return "Draw Card";
		}
	}

}
