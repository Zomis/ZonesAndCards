package net.zomis.cards.mdjq.cards;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQStackAction;

public class TapForMana implements ActivatedAbility {

	private final MColor	color;
	private final int	count;

	public TapForMana(MColor manaColor) {
		this(manaColor, 1);
	}
	public TapForMana(MColor manaColor, int count) {
		this.color = manaColor;
		this.count = count;
	}

	@Override
	public MDJQStackAction getAction(MDJQPermanent card) {
		return new TapForManaAction(card);
	}
	
	private class TapForManaAction extends MDJQStackAction {

		private MDJQPermanent	card;

		public TapForManaAction(MDJQPermanent card) {
			super(ActionType.ACTIVATED);
			this.card = card;
			this.useStack = false;
		}
		@Override
		public boolean isAllowed() {
			return !card.isTapped() && card.getController().hasPriority();
		}
		
		@Override
		public void perform() {
			card.tap();
			card.getController().getManaPool().changeResources(MDJQRes.getMana(color), count);
		}
		
	}

}
