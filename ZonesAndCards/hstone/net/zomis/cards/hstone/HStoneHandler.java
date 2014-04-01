package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.actions.BattlefieldAction;
import net.zomis.cards.hstone.actions.HeroPowerAction;
import net.zomis.cards.hstone.actions.PlayAction;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class HStoneHandler implements ActionHandler {

	@Override
	public StackAction click(Card card) {
		HStoneGame game = (HStoneGame) card.getGame();
		HStonePlayer player = game.getCurrentPlayer();
		
		if (game.isTargetSelectionMode()) {
			return new BattlefieldAction((HStoneCard) card);
		}
		
		if (card.getCurrentZone() == player.getHand()) {
			return playFromHand(card);
		}
		if (card.getCurrentZone() == player.getBattlefield()) {
			return useAtBattlefield(card);
		}
		return new InvalidStackAction("HSTONE_INVALID: " + card + " in zone " + card.getCurrentZone());
	}

	private StackAction useAtBattlefield(Card card) {
		return new BattlefieldAction((HStoneCard) card);
	}

	private StackAction playFromHand(Card card) {
		return new PlayAction((HStoneCard) card);
	}

	@Override
	public List<StackAction> getAvailableActions(CardGame cardGame, Player player) {
		List<StackAction> actions = new ArrayList<StackAction>();
		HStonePlayer currPlayer = (HStonePlayer) player;
		actions.add(new HeroPowerAction(currPlayer));
		for (Card card : currPlayer.getHand().cardList()) {
			actions.add(click(card));
		}
		for (Card card : currPlayer.getBattlefield().cardList()) {
			actions.add(click(card));
		}
		return actions;
	}

}
