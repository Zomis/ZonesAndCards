package net.zomis.cards.crgame;

import net.zomis.cards.model.StackAction;

public class CRUseAction extends StackAction {

	public CRUseAction(CRCard card) {
	}
	
	@Override
	public boolean actionIsAllowed() {
		return false;
	}

}
