package net.zomis.cards.sets;

import java.util.function.Function;

import net.zomis.cards.cbased.CardWithComponents;
import net.zomis.cards.cbased.CompCardModel;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.BattlefieldComponent;
import net.zomis.cards.components.CostComponent;
import net.zomis.cards.components.EffectComponent;
import net.zomis.cards.components.ManaComponent;
import net.zomis.cards.components.PTComponent;
import net.zomis.cards.iface.Component;
import net.zomis.cards.util.CardSet;

public class HSCompCards implements CardSet<FirstCompGame> {

	@Override
	public void addCards(FirstCompGame game) {
//		game.addCard(minion( 1,      FREE, 1, 1, "Stonetusk Boar").charge().card());
//		game.addCard(minion( 1,      FREE, 2, 1, "Voodoo Doctor").battlecry(e.heal(2)).card());
//		game.addCard(minion( 1,    COMMON, 2, 1, "Abusive Sergeant").battlecry(e.tempBoost(f.allMinions(), 2, 0)).card());
//		game.addCard(minion( 1,    COMMON, 1, 1, "Argent Squire").shield().card());
//		game.addCard(minion( 1,    COMMON, 1, 1, "Boar").card());
//		game.addCard(minion( 1,    COMMON, 2, 1, "Damaged Golem").card());
//		game.addCard(minion( 1,    COMMON, 1, 1, "Elven Archer").battlecry(e.damage(1)).card());
//		game.addCard(minion( 1,    COMMON, 0, 4, "Emboldener 3000").on(HStoneTurnEndEvent.class, e.toRandom(f.allMinions(), e.otherPT(1, 1)), f.samePlayer()).card());
//		game.addCard(minion( 1,    COMMON, 1, 2, "Goldshire Footman").taunt().card());
//		game.addCard(minion( 1,    COMMON, 1, 1, "Grimscale Oracle").staticEffectOtherMurlocsBonus(1, 0).is(MURLOC).card());
		
//		game.addCard(minion( 1,    COMMON, 1, 1, "Boar").card());
		game.addCard(new CompCardModel("Boar").addComponents(mana(1), pt(1, 1), minion()));

	}

	private Component pt(int power, int thoughness) {
		return new PTComponent(power, thoughness);
	}

	private Component minion() {
		return new EffectComponent((card) -> card.zoneMoveOnBottom(card.getOwner().getRequiredComponent(BattlefieldComponent.class).getBattlefield()));
	}

	private Component mana(int i) {
		Function<CardWithComponents, ManaComponent> func = src -> src.getOwner().getRequiredComponent(ManaComponent.class);
		return new CostComponent<ManaComponent>("ManaCost " + i, func,
				res -> res.has(i), res -> res.use(i));
	}

}
