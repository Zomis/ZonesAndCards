package net.zomis.cards.cbased;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.events2.DetermineActionEvent;
import net.zomis.cards.events2.FindUsableCardsEvent;
import net.zomis.cards.interfaces.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;

public class ComponentHandler implements ActionHandler {

	private static final StackAction	ILLEGAL	= new InvalidStackAction();

	@Override
	public StackAction click(Card<?> c) {
		CardWithComponents card = (CardWithComponents) c;
		
		StackAction action = card.getGame().executeEvent(new DetermineActionEvent(card)).getAction();
		return action != null ? action : ILLEGAL;
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> gm, Player player) {
		List<Card<?>> result = new ArrayList<>();
		FirstCompGame game = (FirstCompGame) gm;
		
		game.executeEvent(new FindUsableCardsEvent(game, (CompPlayer) player, result));
		
		return result;
	}

}
