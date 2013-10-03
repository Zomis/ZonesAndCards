package net.zomis.cards.simple;

import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.ParamAndField;
import net.zomis.cards.model.AIHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.phases.PlayerPhase;
import net.zomis.cards.simple.cardeffects.PublicStackAction;

public class SimpleAIHandler implements AIHandler {
	
	public SimpleAIHandler() {
	}
	
	@Override
	public void move(CardGame game) {
		PlayerPhase ph = (PlayerPhase) game.getActivePhase();
		SimplePlayer player = (SimplePlayer) ph.getPlayer();
		ParamAndField<SimplePlayer, Card> action = new SimpleCardAI(player).play();
		if (action == null || action.getField() == null)
			return;
		
		game.addAndProcessStackAction(new PlayCardAction(action.getField()));
	}

	@Override
	public StackAction click(Card card) {
		return new PlayCardAction(card);
	}

	@Override
	public List<StackAction> getAvailableActions(Player player) {
		List<StackAction> result = new LinkedList<>();
		for (Card card : ((SimplePlayer)player).getHand().cardList()) {
			SimpleCard model = (SimpleCard) card.getModel();
			PublicStackAction action = model.getAction().createAction(model, card, (SimplePlayer) player);
			if (action.isAllowed()) {
				result.add(action);
			}
		}
//		SimpleGame game = (SimpleGame) player.getGame();
		return result;
	}
	
}
