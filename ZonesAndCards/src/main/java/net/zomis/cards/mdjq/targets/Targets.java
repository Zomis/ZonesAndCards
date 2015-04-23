package net.zomis.cards.mdjq.targets;

import java.util.LinkedList;
import java.util.List;

import net.zomis.cards.mdjq.MDJQTarget;

public class Targets {

	protected List<MDJQTarget> targets;
	
	public Targets() {
		this.targets = new LinkedList<MDJQTarget>();
	}
	
	public void addTargetsFrom(Object object) {
		if (object instanceof HasTargets) {
			HasTargets obj = (HasTargets) object;
			this.targets.addAll(obj.getTargets().targets);
		}
	}
	protected void addTarget(TargetStrategy strategy) {
		this.targets.add(new MDJQTarget(strategy));
	}
	
	public List<MDJQTarget> getTargets() {
		return targets;
	}
	
	public boolean isAllChosen() {
		for (MDJQTarget target : this.targets) {
			if (!target.isTargetChosen())
				return false;
		}
		return true;
	}
	
}
