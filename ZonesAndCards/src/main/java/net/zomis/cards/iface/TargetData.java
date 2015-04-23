package net.zomis.cards.iface;

import java.util.ArrayList;
import java.util.List;

import net.zomis.cards.cbased.CardWithComponents;

public class TargetData {

	private final TargetOptions options;
	private final List<List<CardWithComponents>> cards;

	public TargetData(TargetOptions options) {
		this.options = options;
		this.cards = new ArrayList<>();
	}
	
	public TargetOptions getOptions() {
		return options;
	}
	
	public CardWithComponents getSingleTarget() {
		if (cards.size() != 1) {
			throw new IllegalStateException("Expected only 1 TargetSet but was " + cards.size());
		}
		
		List<CardWithComponents> list = cards.get(0);
		if (list.size() != 1) {
			throw new IllegalStateException("Expected only 1 Target but was " + list.size());
		}
		return list.get(0);
	}
	
}
