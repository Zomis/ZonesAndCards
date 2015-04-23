package net.zomis.cards.cwars2.cards;

import net.zomis.cards.cwars2.CWars2CardFactory;
import net.zomis.cards.cwars2.CWars2Game;
import net.zomis.cards.cwars2.CWars2Res;
import net.zomis.cards.cwars2.CWars2Res.Producers;
import net.zomis.cards.cwars2.RequiresTwo;
import net.zomis.cards.model.actions.PublicAction;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;

public class ZomisCards implements CWarsCardSet {

	public static enum SpecialResource implements IResource {
		SPECIAL;

		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(this, 2, 0, 10);
		}
	}
	
	@Override
	public void addCards(CWars2Game game) {
		game.addResource(SpecialResource.SPECIAL);
		
		CWars2CardFactory slow = new CWars2CardFactory("Slow Down");
		for (Producers prod : Producers.values()) {
			slow.setResourceCost(prod, 1);
			slow.setOppEffect(prod, 1);
		}
		slow.addTo(game);
		
		CWars2CardFactory extraWorkers = new CWars2CardFactory("Extra Workers");
		extraWorkers.setResourceCost(SpecialResource.SPECIAL, 1);
		for (Producers prod : Producers.values()) {
			extraWorkers.setMyEffect(prod, 1);
			extraWorkers.setMyEffect(prod.getResource(), 1);
		}
		extraWorkers.addTo(game);
		
		CWars2CardFactory extraDiscard = new CWars2CardFactory("Extra Discard");
		extraDiscard.setResourceCost(SpecialResource.SPECIAL, 1);
		extraDiscard.setMyEffect(CWars2Res.DISCARDS, 1);
		extraDiscard.addTo(game);
		
		CWars2CardFactory extraSpecial = new CWars2CardFactory("Extra Special");
		for (Producers prod : Producers.values()) {
			extraSpecial.setResourceCost(prod, 2);
			extraSpecial.addAction(new RequiresTwo(game, prod, 3));
		}
		extraSpecial.setMyEffect(SpecialResource.SPECIAL, 1);
		extraSpecial.addTo(game);
		
		CWars2CardFactory extraLOS = new CWars2CardFactory("Extra LOS");
		extraLOS.setResourceCost(SpecialResource.SPECIAL, 1);
		extraLOS.addAction(new ZomisCards.SeeOpponent(game));
		extraLOS.addTo(game);
		
		CWars2CardFactory extraCard = new CWars2CardFactory("Extra Card");
		extraCard.setResourceCost(SpecialResource.SPECIAL, 1);
		extraCard.setMyEffect(CWars2Res.HANDSIZE, 1);
		extraCard.addTo(game);
		
		CWars2CardFactory extraOppLessCard = new CWars2CardFactory("Extra Opponent No Card");
		extraOppLessCard.setResourceCost(SpecialResource.SPECIAL, 2);
		extraOppLessCard.setOppEffect(CWars2Res.HANDSIZE, -1);
		extraOppLessCard.addTo(game);
		
	}
	public static class SeeOpponent extends PublicAction {

		private CWars2Game	game;

		public SeeOpponent(CWars2Game game) {
			this.game = game;
		}
		
		@Override
		public void onPerform() {
			this.game.getCurrentPlayer().getNextPlayer().getHand().setKnown(this.game.getCurrentPlayer(), true);
		}

	}


}
