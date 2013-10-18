package net.zomis.cards.simple.cardeffects;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.simple.SimpleCard;
import net.zomis.cards.simple.SimplePlayer;
import net.zomis.cards.simple.SimpleStackActionFactory;

public class CostDecrease extends StackAction implements SimpleStackActionFactory {

	private SimpleStackAction next;

	public CostDecrease(SimpleStackAction next) {
		this.next = next;
	}

	@Override
	public PublicStackAction createAction(final SimpleCard cardModel, final Card card, final SimplePlayer byPlayer) {
		return new PublicStackAction() {
			@Override
			public boolean isAllowed() {
				if (card.getCurrentZone() == byPlayer.getLibrary())
					return false;
				return byPlayer.getResources() >= cardModel.getCost();
			}
			@Override
			public void onPerform() {
				byPlayer.changeResources(-cardModel.getCost());
				next.perform(cardModel, card, byPlayer);
			}
		};
	}

}
