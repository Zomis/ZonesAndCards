package net.zomis.cards.swing;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;

public class CardViewStrategies {
	
	static class TextCardToString implements CardViewTextStrategy {
		@Override
		public String textFor(Card<?> card) {
			return card.toString();
		}
	}
	static class TextCardModelName implements CardViewTextStrategy {
		@Override
		public String textFor(Card<?> card) {
			return card.getModel().getName();
		}
	}
	static class TextActionString implements CardViewTextStrategy {
		@Override
		public String textFor(Card<?> card) {
			StackAction action = card.getGame().getActionFor(card);
			return String.valueOf(action);
		}
	}

}
