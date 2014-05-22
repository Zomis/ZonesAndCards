package net.zomis.cards.hstone.actions;

import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.model.StackAction;

public class AbilityAction extends StackAction {

	private final HStoneCard	source;

	public AbilityAction(HStoneCard source) {
		this.source = source;
	}
	
	@Override
	public boolean actionIsAllowed() {
		if (!source.hasActionPoints())
			return setErrorMessage("Not enough action points");
		if (!source.getPlayer().getResources().hasResources(HStoneRes.MANA_AVAILABLE, 2))
			return setErrorMessage("Not enough mana: " + source.getPlayer());
		if (!source.getModel().getEffect().hasAnyAvailableTargets(source))
			return setErrorMessage("No available targets");
		return setOKMessage("All OK");
	}
	
	@Override
	protected void onPerform() {
		source.getResources().changeResources(HStoneRes.ACTION_POINTS_USED, 1);
		source.getPlayer().getResources().changeResources(HStoneRes.MANA_AVAILABLE, -2);
		source.getGame().selectOrPerform(source.getModel().getEffect(), source);
		source.getGame().cleanup();
	}
	
}
