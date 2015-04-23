package net.zomis.cards.wart;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.interfaces.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.actions.NextTurnAction;
import net.zomis.cards.wart.actions.AbilityAction;
import net.zomis.cards.wart.actions.BattlefieldAction;
import net.zomis.cards.wart.actions.PlayAction;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.sets.HStoneOption;

public class HStoneHandler implements ActionHandler {

	@Override
	public StackAction click(Card<?> card) {
		HStoneGame game = (HStoneGame) card.getGame();
		
		if (!game.getTemporaryZone().isEmpty()) {
			CardZone<?> zone = card.getCurrentZone();
			if (zone != game.getTemporaryZone())
				return new InvalidStackAction("We're in temporary-zone state");
			return new OptionAction(card, (HStoneOption) card.getModel());
		}
		
		HStoneCard hscard = (HStoneCard) card;
		HStonePlayer player = game.getCurrentPlayer();
		
		if (game.isTargetSelectionMode()) {
			CardZone<?> zone = hscard.getCurrentZone();
			if (zone != hscard.getPlayer().getBattlefield() && hscard != hscard.getPlayer().getPlayerCard())
				return new InvalidStackAction("Illegal zone");
			return new BattlefieldAction(hscard);
		}
		
		if (player == null) {
			if (hscard.getCurrentZone() == hscard.getPlayer().getHand()) {
				return new NextTurnAction(game); // TODO: Implement switching cards -- HStoneOption?
			}
			else return new InvalidStackAction("Pre-Phase and not in hand zone");
		}
		
		if (hscard.getCurrentZone() == player.getHand()) {
			return new PlayAction(hscard);
		}
		if (hscard.getCurrentZone() == player.getBattlefield()) {
			return new BattlefieldAction(hscard);
		}
		if (hscard.getModel().isType(CardType.POWER) && player == hscard.getPlayer()) {
			return new AbilityAction(hscard);
		}
		if (hscard.getModel().isType(CardType.PLAYER) && player == hscard.getPlayer()) {
			return new BattlefieldAction(hscard);
		}
		return new InvalidStackAction("HSTONE_INVALID: " + card + " in zone " + card.getCurrentZone());
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
		List<Card<?>> cards = new ArrayList<Card<?>>();
		
		HStonePlayer currPlayer = (HStonePlayer) player;
		
//		cards.addAll(currPlayer.getGame().getActionZone().cardList()); // This is added by the game
		for (HStoneCard card : currPlayer.getHand()) {
			cards.add(card);
		}
		for (HStoneCard card : currPlayer.getBattlefield()) {
			cards.add(card);
		}
		for (HStoneCard card : currPlayer.getSpecialZone()) {
			cards.add(card);
		}
		
		return cards;
	}

}
