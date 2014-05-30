package net.zomis.cards.components;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import net.zomis.cards.cbased.CardWithComponents;

public class CostComponent<T> implements Component {

	private final Function<CardWithComponents, T> function;
	private final Predicate<T>	requirement;
	private final Consumer<T>	perform;
	private final String	description;

	public CostComponent(String description, Function<CardWithComponents, T> function, Predicate<T> requirement, Consumer<T> perform) {
		this.function = function;
		this.requirement = requirement;
		this.perform = perform;
		this.description = description;
	}

	public void perform(CardWithComponents card) {
		this.perform.accept(this.function.apply(card));
	}
	
	public boolean test(CardWithComponents card) {
		return this.requirement.test(function.apply(card));
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
