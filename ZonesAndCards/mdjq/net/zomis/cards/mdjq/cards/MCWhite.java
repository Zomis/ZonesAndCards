package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQCardModel;
import net.zomis.cards.mdjq.MDJQCardSet;
import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQRes.TribalType;
import net.zomis.cards.mdjq.activated.TapForMana;
import net.zomis.cards.mdjq.effects.LifeToMe;
import net.zomis.cards.mdjq.effects.LifeToTarget;
import net.zomis.cards.mdjq.targets.TargetOpponent;
import net.zomis.cards.mdjq.triggered.ETB;

public class MCWhite implements MDJQCardSet {

	@Override
	public void addCards(MDJQGame game) {
		game.addCard(new MDJQCardModel("Plains").setTypes(CardType.LAND, CardType.BASIC).addActivatedAbility(new TapForMana(MColor.WHITE)));
		game.addCard(new MDJQCardModel("Eager Cadet").setTypes(CardType.CREATURE).setCTypes(TribalType.HUMAN, TribalType.SOLDIER).setPT(1, 1).addCost(MColor.WHITE, 1));
		
		game.addCard(new MDJQCardModel("Test").addCost(MColor.WHITE, 1).setTypes(CardType.CREATURE)
				.setPT(2, 2).setCTypes(TribalType.HUMAN, TribalType.ROUGE, TribalType.MERCENARY).addTrigger(
						new ETB(new LifeToMe(2), new LifeToTarget(new TargetOpponent(), -2))));
		
	}

}
