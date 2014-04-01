package net.zomis.cards.mdjq;

import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.mdjq.activated.ActivatedAbility;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.iterate.CastedIterator;

public class MDJQHandler implements ActionHandler {

	@Override
	public StackAction click(Card c) {
		MDJQPermanent card = (MDJQPermanent) c;
		
		if (card.getCurrentZone().isHand()) {
			return card.playFromHandAction();
		}
		if (card.getCurrentZone() == card.getGame().getBattlefield()) {
			for (ActivatedAbility ee : card.getModel().getActivatedAbilities()) {
				return ee.getAction(card);
			}
		}
		
		return new InvalidStackAction("No Action");
	}

	@Override
	public List<StackAction> getAvailableActions(CardGame cardGame, Player pl) {
		MDJQPlayer player = (MDJQPlayer) pl;
		List<StackAction> actions = new LinkedList<StackAction>();
		for (MDJQPermanent card : new CastedIterator<Card, MDJQPermanent>(player.getHand().cardList())) {
			StackAction act = this.click(card);
			if (act.actionIsAllowed())
				actions.add(act);
		}
		return actions;
	}

}
