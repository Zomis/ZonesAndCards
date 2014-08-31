package net.zomis.cards.events2;

import net.zomis.cards.cbased.CardWithComponents;

public class CompCard1Event extends CompGameEvent {

	private final CardWithComponents source;

	public CompCard1Event(CardWithComponents source) {
		super(source.getGame());
		this.source = source;
	}

	public CardWithComponents getSource() {
		return source;
	}
	
}
