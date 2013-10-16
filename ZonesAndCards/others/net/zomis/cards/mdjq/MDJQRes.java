package net.zomis.cards.mdjq;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceType;


public final class MDJQRes {
	private static final Map<MColor, ResourceType> manas = new HashMap<MColor, ResourceType>();
	private static MDJQRes res = new MDJQRes();
	@Deprecated
	static MDJQRes getRes() {
		return res;
	}
	
	public static enum CardType {
		BASIC, LAND, ARTIFACT, CREATURE, ENCHANTMENT, PLANESWALKER, INSTANT, SORCERY;

		public boolean isPermanent() {
			return this == LAND || this == ARTIFACT || this == CREATURE || this == ENCHANTMENT || this == PLANESWALKER;
		}
	}
	public static enum TribalType {
		HUMAN, SOLDIER, ROUGE, MERCENARY;
	}
	
	private static final ResourceType power = new ResourceType("Power");
	private static final ResourceType toughness = new ResourceType("Toughness");
	public static ResourceType getMana(MColor color) {
		return manas.get(color);
	}
	
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
	
	public static enum MColor {
		COLORLESS, WHITE, BLACK;
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
	
}
