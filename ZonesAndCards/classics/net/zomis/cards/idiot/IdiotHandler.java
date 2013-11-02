package net.zomis.cards.idiot;

import java.util.LinkedList;
import java.util.List;

import net.zomis.ZomisList;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.scorers.SubclassFixedScorer;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.ai.CardAI;

public class IdiotHandler implements ActionHandler {

	public static class IdiotGameAI extends CardAI {
		public IdiotGameAI(CardGame game) {
			super();
			
			ScoreConfigFactory<Player, StackAction> config = new ScoreConfigFactory<Player, StackAction>();
			config.withScorer(new SubclassFixedScorer<Player, StackAction, RemoveAction>(RemoveAction.class), 10);
			config.withScorer(new SubclassFixedScorer<Player, StackAction, MoveAction>(MoveAction.class), 5);
			config.withScorer(new SubclassFixedScorer<Player, StackAction, DealAction>(DealAction.class), 1);
			this.setConfig(config.build());
		}
	}
	
	@Override
	public StackAction click(Card card) {
		IdiotGame game = (IdiotGame) card.getGame();
		if (card.getCurrentZone() == game.getDeck()) {
			return new DealAction(game);
		}
		
		if (card.getCurrentZone().cardList().peekLast() != card)
			return new InvalidStackAction("Not last card");
		
		RemoveAction remove = new RemoveAction((ClassicCardZone) card.getCurrentZone());
		if (remove.actionIsAllowed())
			return remove;
		
		List<StackAction> random = new LinkedList<StackAction>();
		for (ClassicCardZone zone : game.getIdiotZones()) {
			if (zone.cardList().isEmpty()) {
				random.add(new MoveAction(card, zone));
			}
		}
		if (random.isEmpty())
			return new InvalidStackAction("Nowhere to move");
		return ZomisList.getRandom(random);
	}

	@Override
	public List<StackAction> getAvailableActions(CardGame cardGame, Player player) {
		IdiotGame game = (IdiotGame) player.getGame();
		LinkedList<StackAction> result = new LinkedList<StackAction>();
		result.add(new DealAction(game));
		
		for (ClassicCardZone idiots : game.getIdiotZones()) {
			if (idiots.isEmpty())
				continue;
			result.add(new RemoveAction(idiots));
			for (ClassicCardZone idiot2 : game.getIdiotZones()) {
				result.add(new MoveAction(idiots.cardList().peekLast(), idiot2));
			}
		}
		return result;
	}

}
