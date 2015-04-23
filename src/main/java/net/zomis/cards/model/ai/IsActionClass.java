package net.zomis.cards.model.ai;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;

public class IsActionClass extends AbstractScorer<Player, Card<?>> {

	private Class<?>	clazz;

	public IsActionClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public double getScoreFor(Card<?> field, ScoreParameters<Player> scores) {
		return field.clickAction().getClass() == clazz ? 1 : 0;
	}
	
}
