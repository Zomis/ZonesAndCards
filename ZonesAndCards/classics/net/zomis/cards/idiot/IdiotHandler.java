package net.zomis.cards.idiot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.ActionHandler;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.model.actions.InvalidStackAction;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.model.ai.IsActionClass;
import net.zomis.utils.ZomisList;

public class IdiotHandler implements ActionHandler {

	public static class IdiotGameAI extends CardAI {
		public IdiotGameAI(IdiotGame game) {
			super();
			
			ScoreConfigFactory<Player, Card<?>> config = new ScoreConfigFactory<Player, Card<?>>();
			config.withScorer(new IsActionClass(RemoveAction.class), 10);
			config.withScorer(new IsActionClass(MoveAction.class), 5);
			config.withScorer(new AbstractScorer<Player, Card<?>>() {
				@Override
				public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
					if (!(field.clickAction() instanceof MoveAction))
						return 0;
					MoveAction cast = (MoveAction) field.clickAction();
					return cast.getSource().size() > 1 ? 0 : 1; // only move if it makes some sense in moving
				}
			}, -5);
			config.withScorer(new IsActionClass(DealAction.class), 1);
			this.setConfig(config.build());
		}
	}
	
	@Override
	public StackAction click(Card<?> card) {
		IdiotGame game = (IdiotGame) card.getGame();
		if (card.getCurrentZone() == game.getDeck()) {
			return new DealAction(game);
		}
		
		if (card.getCurrentZone().getBottomCard() != card)
			return new InvalidStackAction("Not last card");
		
		RemoveAction remove = new RemoveAction((ClassicCardZone) card.getCurrentZone());
		if (remove.actionIsAllowed())
			return remove;
		
		List<StackAction> random = new LinkedList<StackAction>();
		for (ClassicCardZone zone : game.getIdiotZones()) {
			if (zone.isEmpty()) {
				random.add(new MoveAction(card, zone));
			}
		}
		if (random.isEmpty())
			return new InvalidStackAction("Nowhere to move");
		return ZomisList.getRandom(random);
	}

	@Override
	public List<Card<?>> getUseableCards(CardGame<? extends Player, ? extends CardModel> game, Player player) {
		List<Card<?>> cards = new ArrayList<Card<?>>();
		
		
		return cards;
	}

}
