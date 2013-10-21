package net.zomis.cards.cwars2;

import net.zomis.ZomisUtils;
import net.zomis.cards.util.IResource;

public enum CWars2Res implements IResource {
	CASTLE(25), WALL(15);
	
	private int	defaultValue;
	private CWars2Res(int mdefault) {
		this.defaultValue = mdefault;
	}
	
	@Override
	public int getDefault() {
		return this.defaultValue;
	}
	
	public static enum Resources implements IResource {
		BRICKS, WEAPONS, CRYSTALS;
		
		public static final int MIN = 0;
		@Override
		public int getDefault() {
			return 8;
		}
		public Producers getProducer() {
			return Producers.values()[this.ordinal()];
		}
		@Override
		public String toString() {
			return ZomisUtils.capitalize(super.toString());
		}
	}
	public static enum Producers implements IResource {
		BUILDERS, RECRUITS, WIZARDS;
		public static final int MIN = 0;
		@Override
		public int getDefault() {
			return 2;
		}
		public Resources getResource() {
			return Resources.values()[this.ordinal()];
		}
		@Override
		public String toString() {
			return ZomisUtils.capitalize(super.toString());
		}
	}

}
