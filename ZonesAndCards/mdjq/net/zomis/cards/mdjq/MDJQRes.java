package net.zomis.cards.mdjq;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;


public final class MDJQRes {
	private static final Map<MColor, ResourceType> manas = new HashMap<MColor, ResourceType>();
	
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
	
	private static final ResourceType power = new ResourceType("Power");
	private static final ResourceType xCost = new ResourceType("X");
	private static final ResourceType toughness = new ResourceType("Toughness");
	public static ResourceType getMana(MColor color) {
		return manas.get(color);
	}
	static final MDJQRes usedForInitialization = new MDJQRes();
	private MDJQRes() {
		for (MColor c : MColor.values())
			manas.put(c, new ResourceType(c.toString()));
	}
	public static ResourceType getPower() {
		return power;
	}
	public static ResourceType getToughness() {
		return toughness;
	}
	public static ResourceType getXCost() {
		return xCost;
	}
	public static enum MColor {
		COLORLESS, WHITE, BLUE, BLACK, RED, GREEN;
	}

	public static boolean hasResources(ResourceMap manaPool, ResourceMap manaCost) {
		manaPool = new ResourceMap(manaPool);
		manaCost = new ResourceMap(manaCost);
		
		changeResources(manaPool, manaCost, -1);
		
		for (Entry<ResourceType, Integer> ee : manaPool.getValues()) {
			if (ee.getValue() < 0) {
				return false;
			}
		}
		return true;
	}

	public static void changeResources(ResourceMap manaPool, ResourceMap changes, int multiplier) {
		int colorless = 0;
		changes = new ResourceMap(changes);
		
		for (Entry<ResourceType, Integer> mana : manaPool.getValues()) {
			Integer change = changes.getResources(mana.getKey());
			if (change == null)
				continue;
			manaPool.changeResources(mana.getKey(), change * multiplier);
			changes.changeResources(mana.getKey(), -change);
		}
		
		for (Entry<ResourceType, Integer> ee : changes.getValues()) {
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
		
		for (Entry<ResourceType, Integer> ee : manaPool.getValues()) {
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

	private static ResourceType colorlessMana() {
		return getMana(MColor.COLORLESS);
	}
	public static ResourceMap manaCost(MColor black, int i) {
		
		return null;
	}
	
}
