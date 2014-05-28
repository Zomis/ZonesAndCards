package net.zomis.cards.cbased;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.events2.CardClickEvent;
import net.zomis.cards.events2.FindUsableCardsEvent;
import net.zomis.cards.model.ActionHandler;
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
		@SuppressWarnings("unchecked")
		CardWithComponents<CompCardModel> card = (CardWithComponents<CompCardModel>) c;
		
		StackAction action = card.getGame().executeEvent(new CardClickEvent(card)).getAction();
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
