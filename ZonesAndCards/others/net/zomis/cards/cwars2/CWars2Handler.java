package net.zomis.cards.cwars2;

import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;

public class CWars2Handler implements ActionHandler {

	@Override
	public StackAction click(Card card) {
		CWars2Game game = (CWars2Game) card.getGame();
		if (card.getCurrentZone() == game.getDiscard()) {
			return new ToggleDiscardAction(game);
		}
		if (game.isDiscardMode()) {
			return new CWars2DiscardAction(card);
		}
		
		return new CWars2PlayAction(card, (CWars2Card) card.getModel());
	}

	@Override
	public List<StackAction> getAvailableActions(Player player) {
		List<StackAction> list = new LinkedList<StackAction>();
		CWars2Player pl = (CWars2Player) player;
		for (Card card : pl.getHand().cardList()) {
			CWars2Card model = (CWars2Card) card.getModel();
			list.add(new CWars2PlayAction(card, model)); // play actions first
		}
		
		for (Card card : pl.getHand().cardList()) {
			list.add(new CWars2DiscardAction(card)); // then discard actions
		}
		
		if (pl.getGame().getDiscarded() > 0) {
			list.add(new NextTurnAction(player.getGame()));
		}
		return list;
	}

}
