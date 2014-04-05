package net.zomis.cards.hstone;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.hstone.actions.BattlefieldAction;
import net.zomis.cards.hstone.actions.HeroPowerAction;
import net.zomis.cards.hstone.actions.PlayAction;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class HStoneHandler implements ActionHandler {

	@Override
	public StackAction click(Card<?> card) {
		HStoneCard hscard = (HStoneCard) card;
		HStoneGame game = hscard.getGame();
		HStonePlayer player = game.getCurrentPlayer();
		
		if (game.isTargetSelectionMode()) {
			return new BattlefieldAction((HStoneCard) card);
		}
		
		if (card.getCurrentZone() == player.getHand()) {
			return playFromHand(hscard);
		}
		if (card.getCurrentZone() == player.getBattlefield()) {
			return useAtBattlefield(hscard);
		}
		return new InvalidStackAction("HSTONE_INVALID: " + card + " in zone " + card.getCurrentZone());
	}

	private StackAction useAtBattlefield(HStoneCard card) {
		return new BattlefieldAction(card);
	}

	private StackAction playFromHand(HStoneCard card) {
		return new PlayAction(card);
	}

	@Override
	public <E extends CardGame<Player, CardModel>> List<StackAction> getAvailableActions(E cardGame, Player player) {
		List<StackAction> actions = new ArrayList<StackAction>();
		HStonePlayer currPlayer = (HStonePlayer) player;
		actions.add(new HeroPowerAction(currPlayer));
		for (HStoneCard card : currPlayer.getHand().cardList()) {
			actions.add(click(card));
		}
		for (HStoneCard card : currPlayer.getBattlefield().cardList()) {
			actions.add(click(card));
		}
		return actions;
	}

}
