package net.zomis.cards.mdjq.actions;

import net.zomis.cards.mdjq.MDJQPermanent;
import net.zomis.cards.mdjq.cards.TriggerData;
import net.zomis.cards.mdjq.cards.TriggeredEffect;
import net.zomis.cards.mdjq.targets.HasTargets;

public class CombinedEffect extends MDJQTargetAction {

	private TriggeredEffect[] effects;
	private TriggerData	data;

	public CombinedEffect(MDJQPermanent permanent, TriggeredEffect[] effect) {
		super(ActionType.TRIGGERED, permanent);
		this.effects = effect;
		for (TriggeredEffect eff : effect)
			this.getTargets().addTargetsFrom(eff);
		this.data = new TriggerData(permanent);
	}
	
	@Override
	public boolean isAllowed() {
		for (TriggeredEffect effect : effects) {
			if (effect instanceof HasTargets) {
				HasTargets eff = (HasTargets) effect;
				if (!eff.getTargets().isAllChosen())
					return false;
			}
		}
		return true;
	}
	
	@Override
	public void onPerform() {
		for (TriggeredEffect trig : effects) {
			trig.apply(data);
		}
	}

}
