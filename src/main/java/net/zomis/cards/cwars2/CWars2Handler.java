package net.zomis.cards.cwars2;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.interfaces.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.NextTurnAction;

public class CWars2Handler implements ActionHandler {

	@Override
	public StackAction click(Card<?> card) {
		@SuppressWarnings("unchecked")
		Card<CWars2Card> ccard = (Card<CWars2Card>) card;
		CWars2Game game = (CWars2Game) card.getGame();
		
		if (ccard == game.getActionDiscardCard()) {
			return new ToggleDiscardAction(game);
		}
		if (ccard == game.getActionNextTurnCard()) {
			return new NextTurnAction(game);
		}
		
		for (CWars2Player pl : game.getPlayers()) {
			if (card.getCurrentZone() == pl.getDiscard()) {
				return new ToggleDiscardAction(game);
			}
		}
		
		if (game.isDiscardMode()) {
			return new CWars2DiscardAction(ccard);
		}
		
		return new CWars2PlayAction(ccard);
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
		List<Card<?>> cards = new ArrayList<Card<?>>();
		
		CWars2Player pl = (CWars2Player) player;
		for (Card<CWars2Card> card : pl.getHand()) {
			cards.add(card);
		}
		
		cards.add(pl.getGame().getActionDiscardCard());
		cards.add(pl.getGame().getActionNextTurnCard());
		
		return cards;
	}

}
