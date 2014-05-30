package net.zomis.cards.systems;

import java.util.function.Function;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.Component;
import net.zomis.cards.components.CostComponent;
import net.zomis.cards.components.EffectComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.ResourceMWKComponent;
import net.zomis.cards.util.CardSet;

public class MWKCardsSystem implements CardSet<FirstCompGame> {

	@Override
	public void addCards(FirstCompGame game) {
		game.addCard(new CompCardModel("Attack").addComponent(required(0, 3, 0)).addComponent(damage(5)));
		game.addCard(new CompCardModel("Recruit").addComponent(required(2, 0, 0)).addComponent(myRes(0, 5, 0)));
		game.addCard(new CompCardModel("Panic Kingdom").addComponent(required(0, 0, 1)).addComponent(myRes(10, 10, 0)));
		game.addCard(new CompCardModel("Pick a King, Any King").addComponent(required(6, 6, 0)).addComponent(myRes(0, 0, 1)));
		game.addCard(new CompCardModel("Heal Me").addComponent(required(2, 0, 0)).addComponent(selfHeal(4)));
		
		game.addCard(new CompCardModel("All-In!").addComponent(required(0, 10, 0)).addComponent(damage(15)));
		game.addCard(new CompCardModel("Mega Heal").addComponent(required(5, 0, 0)).addComponent(selfHeal(11)));
		game.addCard(new CompCardModel("Transforming").addComponent(required(0, 2, 0)).addComponent(myRes(3, 0, 0)));
		game.addCard(new CompCardModel("Wizards in the city").addComponent(required(0, 2, 0)).addComponent(myRes(4, 0, 0)));
		game.addCard(new CompCardModel("Slay Kingdom").addComponent(required(7, 7, 0)).addComponent(destroyOppRes()));
	}

	private Component destroyOppRes() {
		return new EffectComponent("Destroy opponent mages and warriors", src -> {
			src.getOwner().getNextPlayer().getRequiredComponent(ResourceMWKComponent.class).setMages(0);
			src.getOwner().getNextPlayer().getRequiredComponent(ResourceMWKComponent.class).setWarriors(0);
		});
	}

	private Component selfHeal(int i) {
		return new EffectComponent("Heal " + i, src -> src.getOwner().getRequiredComponent(HealthComponent.class).heal(i));
	}

	private Component myRes(int mages, int warriors, int kings) {
		return new EffectComponent(new DescribedEffect("Add " + mwkString(mages, warriors, kings), src -> src.getOwner().getRequiredComponent(ResourceMWKComponent.class).change(mages, warriors, kings)));
	}

	private String mwkString(int mages, int warriors, int kings) {
		StringBuilder str = new StringBuilder();
		if (mages != 0) {
			if (str.length() > 0) str.append(", ");
			str.append(mages + " mages");
		}
		if (warriors != 0) {
			if (str.length() > 0) str.append(", ");
			str.append(warriors + " warriors");
		}
		if (kings != 0) {
			if (str.length() > 0) str.append(", ");
			str.append(kings + " kings");
		}
		return str.toString();
	}

	private Component damage(int i) {
		return new EffectComponent(new DescribedEffect("Damage " + i, src -> src.getOwner().getNextPlayer().getRequiredComponent(HealthComponent.class).damage(i)));
	}

	private Component required(int mages, int warriors, int kings) {
		Function<CardWithComponents, ResourceMWKComponent> func = src -> src.getOwner().getRequiredComponent(ResourceMWKComponent.class);
		return new CostComponent<ResourceMWKComponent>("Costs " + mwkString(mages, warriors, kings), func,
				res -> res.has(mages, warriors, kings), res -> res.change(-mages, -warriors, -kings));
	}

}
