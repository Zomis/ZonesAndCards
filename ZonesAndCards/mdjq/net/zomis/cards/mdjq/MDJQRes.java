package net.zomis.cards.mdjq;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;


public final class MDJQRes {
	private MDJQRes() {}
	
	public static enum CardType {
		BASIC, LAND, ARTIFACT, CREATURE, ENCHANTMENT, PLANESWALKER, INSTANT, SORCERY, LEGENDARY, TRIBAL;

		public boolean isPermanent() {
			return this == LAND || this == ARTIFACT || this == CREATURE || this == ENCHANTMENT || this == PLANESWALKER;
		}
	}
	public static class TribalType {
		private static Map<String, TribalType> types = new HashMap<String, TribalType>();
		private final String	type;
		
		private TribalType(String type) {
//			CustomFacade.getLog().i("Creating TribalType: " + type);
			this.type = type;
			types.put(type, this);
		}
		
		public static TribalType valueOf(String type) {
			if (types.containsKey(type))
				return types.get(type);
			else return new TribalType(type);
		}
		
		@Override
		public String toString() {
			return type;
		}
		
		public static TribalType HUMAN = new TribalType("Human");
		public static TribalType SOLDIER = new TribalType("Soldier");
		public static TribalType ROUGE = new TribalType("Rouge");
		public static TribalType MERCENARY = new TribalType("Mercenary");
	}
	
	public static final ResourceType power = new ResourceType("Power");
	public static final ResourceType xCost = new ResourceType("X");
	public static final ResourceType toughness = new ResourceType("Toughness");
	
	@Deprecated
	public static IResource getMana(MColor color) {
		return color;
	}
	static final MDJQRes usedForInitialization = new MDJQRes();
	public static ResourceType getPower() {
		return power;
	}
	public static ResourceType getToughness() {
		return toughness;
	}
	public static ResourceType getXCost() {
		return xCost;
	}
	public static enum MColor implements IResource {
		COLORLESS, WHITE, BLUE, BLACK, RED, GREEN;
		@Override
		public int getDefault() {
			return 0;
		}
	}

	public static boolean hasResources(ResourceMap manaPool, ResourceMap manaCost) {
		manaPool = new ResourceMap(manaPool);
		manaCost = new ResourceMap(manaCost);
		
		changeResources(manaPool, manaCost, -1);
		
		for (Entry<IResource, Integer> ee : manaPool.getValues()) {
			if (ee.getValue() < 0) {
				return false;
			}
		}
		return true;
	}

	public static void changeResources(ResourceMap manaPool, ResourceMap changes, int multiplier) {
		int colorless = 0;
		changes = new ResourceMap(changes);
		
		for (Entry<IResource, Integer> mana : manaPool.getValues()) {
			Integer change = changes.getResources(mana.getKey());
			if (change == null)
				continue;
			manaPool.changeResources(mana.getKey(), change * multiplier);
			changes.changeResources(mana.getKey(), -change);
		}
		
		for (Entry<IResource, Integer> ee : changes.getValues()) {
			if (ee.getValue() == 0)
				continue;
			if (ee.getKey() == getMana(MColor.COLORLESS)) {
				colorless = ee.getValue();
			}
			else {
				manaPool.set(ee.getKey(), ee.getValue() * multiplier);
				changes.set(ee.getKey(), -ee.getValue());
			}
		}
		
		for (Entry<IResource, Integer> ee : manaPool.getValues()) {
			if (colorless == 0)
				return;
			if (ee.getValue() <= 0)
				continue;
			
			int colorChange = Math.min(colorless, ee.getValue());
			manaPool.changeResources(ee.getKey(), -colorChange);
			changes.changeResources(colorlessMana(), -colorChange);
			colorless -= colorChange;
		}
		
		manaPool.changeResources(colorlessMana(), colorless * multiplier);
	}

	private static IResource colorlessMana() {
		return getMana(MColor.COLORLESS);
	}
	public static ResourceMap manaCost(MColor color, int value) {
		return new ResourceMap().set(color, value);
	}
	
}
