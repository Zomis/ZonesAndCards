package net.zomis.cards.crgame;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class CRHandler implements ActionHandler {

	private static final StackAction INVALID_ZONE = new InvalidStackAction("Invalid zone");
	
	@Override
	public StackAction click(Card<?> cc) {
		CRCard card = (CRCard) cc;
		
		if (card.getGame().isTargetSelectionMode()) {
			return new CRTargetChosenAction(card);
		}
		
		if (card.getCurrentZone() == card.getPlayer().getBattlefield()) {
			return new CRUseAction(card);
		}
		
		if (card.getCurrentZone() == card.getPlayer().getHand())
			return new CRPlayAction(card);
		
		return INVALID_ZONE;
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
		CRPlayer pl = (CRPlayer) player;
		List<Card<?>> results = new ArrayList<Card<?>>();
		for (Card<?> card : pl.getHand())
			results.add(card);
		return results;
	}

}
