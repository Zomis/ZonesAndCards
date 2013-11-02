package net.zomis.cards.cwars2;

import net.zomis.ZomisUtils;
import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceData;

public enum CWars2Res implements IResource {
	CASTLE(25), WALL(15);
	
	private final int	defaultValue;
	private CWars2Res(int mdefault) {
		this.defaultValue = mdefault;
	}
	
	public static enum Resources implements IResource {
		BRICKS, WEAPONS, CRYSTALS;
		public Producers getProducer() {
			return Producers.values()[this.ordinal()];
		}
		@Override
		public String toString() {
			return ZomisUtils.capitalize(super.toString());
		}
		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(resource, 8, 0, Integer.MAX_VALUE);
		}
	}
	public static enum Producers implements IResource {
		BUILDERS, RECRUITS, WIZARDS;
		public Resources getResource() {
			return Resources.values()[this.ordinal()];
		}
		@Override
		public String toString() {
			return ZomisUtils.capitalize(super.toString());
		}
		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(resource, 2, 1, Integer.MAX_VALUE);
		}
	}
	@Override
	public ResourceData createData(IResource resource) {
		return ResourceData.forResource(resource, this.defaultValue);
	}

}
