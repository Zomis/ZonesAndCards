package net.zomis.cards.count;

import net.zomis.cards.model.CardModel;


public class CardCount<M extends CardModel> {

	public static interface Callback {
		void onChanged(int oldValue, int newValue);
	}
	private final M model;
	private int count;
	private Callback onChange;
	
	public CardCount(M model, Callback callback) {
		this.model = model;
		this.onChange = callback;
	}
	
	public int getCount() {
		return count;
	}
	
	public M getModel() {
		return model;
	}
	
	public void setCount(int count) {
		int old = this.count;
		this.count = count;
		if (onChange != null)
			onChange.onChanged(old, count);
	}
}
