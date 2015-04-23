package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.events.CardGameEvent;
import net.zomis.events.IEvent;

public class AttackEvent extends CardGameEvent implements IEvent {

	private final CardWithComponents source;
	private final CardWithComponents target;

	public AttackEvent(CardWithComponents source, CardWithComponents target) {
		super(source.getGame());
		this.source = source;
		this.target = target;
	}
	
	@Override
	public FirstCompGame getGame() {
		return (FirstCompGame) super.getGame();
	}
	
	public CardWithComponents getSource() {
		return source;
	}
	
	public CardWithComponents getTarget() {
		return target;
	}

}
