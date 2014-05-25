package net.zomis.cards.wart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HStoneCardFactory;
import net.zomis.cards.wart.factory.HStoneRarity;
import net.zomis.utils.ZomisUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WSBaseCard {
	protected HStoneCardFactory factory;
	
	@JsonCreator
	public WSBaseCard(String name, int manaCost) {
		this.name = name;
		this.cost = manaCost;
		factory = new HStoneCardFactory(name, manaCost, getCardType());
	}
	
	protected CardType getCardType() {
		throw new UnsupportedOperationException();
	}

	public String name;
	public List<String> descs = Collections.emptyList();
	public int cost;
	public HStoneRarity rarity;
	public void setRarity(String rarity) {
		this.rarity = HStoneRarity.valueOf(rarity.toUpperCase().substring(2));
	}
	
	public HStoneClass clazz;
	
	@JsonProperty("class")
	public void setClazz(String clazz) {
		if (clazz.isEmpty() || clazz.toUpperCase().equals("ANY")) {
			this.clazz = null;
			return;
		}
		this.clazz = HStoneClass.valueOf(clazz.toUpperCase());
	}
	
	public String funText;
	public String fullDesc;
	
	public void setDesc(String desc) {
		try {
			this.descs = new ArrayList<>(Arrays.asList(new ObjectMapper().readValue(desc, String[].class)));
			this.fullDesc = descs.toString();
			Predicate<String> isA = s -> s.contains("<i>");
			funText = descs.stream().filter(isA).findAny().orElse(null);
			descs.removeIf(isA);
			descs.removeIf(String::isEmpty);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return getCardType() + " [name=" + name + ", descs="
				+ descs + ", cost=" + cost + ", rarity="
				+ rarity + ", clazz=" + clazz + "]";
	}

	public int parseDesc() {
		int count = 0;
		for (int i = 0; i < descs.size(); i++) {
			if (descs.get(i).endsWith("Overload:</b>")) {
//				System.out.println("Overload found: " + this);
				String value = descs.get(i + 1);
				factory.overload(Integer.parseInt(ZomisUtils.textBetween(value, "(", ")")));
				descs.remove(i);
				descs.remove(i);
				count += 2;
				break;
			}
		}
		
		return count;
	}

}
