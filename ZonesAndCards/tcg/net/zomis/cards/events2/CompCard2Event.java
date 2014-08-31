package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;

public class CompCard2Event extends CompGameEvent {

	private final CardWithComponents source;
	private final CardWithComponents target;

	public CompCard2Event(CardWithComponents source, CardWithComponents target) {
		super(source.getGame());
		this.source = source;
		this.target = target;
	}

	public CardWithComponents getSource() {
		return source;
	}
	
	public CardWithComponents getTarget() {
		return target;
	}
	
}
