package net.zomis.cards.idiot;

import net.zomis.cards.classics.ClassicCard;
import net.zomis.cards.classics.ClassicCardZone;
import net.zomis.cards.model.actions.ZoneMoveAction;

public class RemoveAction extends ZoneMoveAction {

	public RemoveAction(ClassicCardZone idiot) {
		super(idiot.cardList().peekLast());
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
			if (zone.cardList().isEmpty())
				continue;
			
			ClassicCard top = (ClassicCard) zone.cardList().peekLast().getModel();
			if (top.getSuite() != model.getSuite())
				continue;
			
			if (top.getRank() > model.getRank())
				return true;
		}
		return false;
	}
	
}
