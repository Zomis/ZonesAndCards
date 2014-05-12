package net.zomis.cards.crgame;

import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;

public enum CRRes implements IResource {
	HOURS_COST, HOURS_AVAILABLE, QUALITY;

	@Override
	public ResourceData createData(IResource resource) {
		return ResourceData.forResource(resource);
	}

}
