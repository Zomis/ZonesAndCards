package net.zomis.cards.interfaces;

import java.util.List;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;


public interface ActionHandler {
	StackAction click(Card<?> card);
	
	List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player);
}
