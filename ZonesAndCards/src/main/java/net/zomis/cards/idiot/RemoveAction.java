package net.zomis.cards.idiot;

import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class RemoveAction extends ZoneMoveAction {

	public RemoveAction(ClassicCardZone idiot) {
		super(idiot.getBottomCard());
		this.setDestination(null);
	}

	@Override
	public boolean actionIsAllowed() {
		if (this.getCard() == null)
			return false;
		
		IdiotGame game = (IdiotGame) getCard().getGame();
		ClassicCard model = (ClassicCard) getCard().getModel();
		
		for (ClassicCardZone zone : game.getIdiotZones()) {
			if (zone == this.getCard().getCurrentZone())
				continue;
			if (zone.isEmpty())
				continue;
			
			ClassicCard lastCard = (ClassicCard) zone.getBottomCard().getModel();
			if (lastCard.getSuite() != model.getSuite())
				continue;
			
			if (lastCard.getRank() > model.getRank())
				return true;
		}
		return false;
	}
	
}
