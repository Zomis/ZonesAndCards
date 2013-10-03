package net.zomis.cards.cwars2;

import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.ai.RandomAI;

public class CWars2Handler implements AIHandler {

	@Override
	public void move(CardGame game) {
		ParamAndField<Player, StackAction> act = new RandomAI(game).play();
		if (act != null)
			game.addAndProcessStackAction(act.getField());
	}

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
			list.add(new CWars2PlayAction(card, model));
			list.add(new CWars2DiscardAction(card));
		}
		
		return list;
	}

}
