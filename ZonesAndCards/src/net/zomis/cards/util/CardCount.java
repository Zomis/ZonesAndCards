package net.zomis.cards.util;

import net.zomis.cards.model.CardModel;


public class CardCount {

	public static interface Callback {
		void onChanged(int oldValue, int newValue);
	}
	private final CardModel model;
	private int count;
	private Callback onChange;
	
	public CardCount(CardModel model, Callback callback) {
		this.model = model;
		this.onChange = callback;
	}
	
	public int getCount() {
		return count;
	}
	
	public CardModel getModel() {
		return model;
	}
	
	public void setCount(int count) {
		int old = this.count;
		this.count = count;
		if (onChange != null)
			onChange.onChanged(old, count);
	}
}
