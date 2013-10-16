package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQCardModel;
import net.zomis.cards.mdjq.MDJQCardSet;
import net.zomis.cards.mdjq.MDJQGame;
import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQRes.TribalType;

public class MCBlack implements MDJQCardSet {

	@Override
	public void addCards(MDJQGame game) {
		game.addCard(new MDJQCardModel("Swamp").setTypes(CardType.LAND, CardType.BASIC).addActivatedAbility(new TapForMana(MDJQRes.MColor.BLACK)));
		game.addCard(new MDJQCardModel("Highway Robber").addCost(MColor.BLACK, 2).addCost(MColor.COLORLESS, 2).setTypes(CardType.CREATURE)
				.setPT(2, 2).setCTypes(TribalType.HUMAN, TribalType.ROUGE, TribalType.MERCENARY).addTrigger(
						new ETB(new LifeToMe(2), new LifeToTarget(new TargetOpponent(), -2))));
	}

}
