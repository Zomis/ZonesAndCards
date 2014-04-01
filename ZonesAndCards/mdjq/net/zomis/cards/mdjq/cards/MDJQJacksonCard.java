package net.zomis.cards.mdjq.cards;

import java.util.Scanner;

import net.zomis.cards.mdjq.MDJQRes;
import net.zomis.cards.mdjq.MDJQRes.CardType;
import net.zomis.cards.mdjq.MDJQRes.MColor;
import net.zomis.cards.mdjq.MDJQRes.TribalType;
import net.zomis.cards.resources.ResourceMap;
import net.zomis.custommap.CustomFacade;
import net.zomis.utils.ZomisUtils;

public class MDJQJacksonCard {
	public int id;
	public String name;
	
	private ResourceMap cost;
	
	public void setCost(String values) {
		this.cost = new ResourceMap();
		Scanner scan = new Scanner(values);
		scan.useDelimiter("\\}");
		String curr;
		while (scan.hasNext()) {
			curr = scan.next();
			if (curr.length() < 2) break;
			if (curr.length() > 2)
				CustomFacade.getLog().w("Not implemented complete support for '" + curr.substring(1) + "' in card " + this.name);
			char c = curr.charAt(1);
			if (c == 'W') this.cost.changeResources(MColor.WHITE, 1);
			else if (c == 'U') this.cost.changeResources(MColor.BLUE, 1);
			else if (c == 'B') this.cost.changeResources(MColor.BLACK, 1);
			else if (c == 'R') this.cost.changeResources(MColor.RED, 1);
			else if (c == 'G') this.cost.changeResources(MColor.GREEN, 1);
			else if (c == 'X') this.cost.changeResources(MDJQRes.getXCost(), 1);
			else {
				this.cost.changeResources(MColor.COLORLESS, Integer.parseInt(curr.substring(1)));
			}
		}
		scan.close();
	}
	
	public ResourceMap getCost() {
		return cost;
	}
	
	public String type;
	public String power;
	public String toughness;
	public String oracleText;
	public String edition;
	public String rarity;
	public double rating;
	public String artist;
	public int num;
	public String text;
	
	@Override
	public String toString() {
		return String.format("'%s' (%s) %s/%s: %s", this.name, this.type, this.power, this.toughness, this.text);
	}
	
	public void setType(String string) {
		if (string == null) {
			CustomFacade.getLog().w("Type set to null: " + this);
			return;
		}
		
		if (string.contains(" - ")) {
			String mainTypes = ZomisUtils.textBefore(string, " - ");
			String[] types = mainTypes.split(" ");
			CardType[] realTypes = new CardType[types.length];
			for (int i = 0; i < types.length; i++) {
				realTypes[i] = CardType.valueOf(types[i].toUpperCase());
			}
			
			String postTypes = ZomisUtils.textAfter(string, " - ");
			types = postTypes.split(" ");
			TribalType[] tribalTypes = new TribalType[types.length];
			for (int i = 0; i < types.length; i++) {
				tribalTypes[i] = TribalType.valueOf(types[i]);
			}
			
		}
		this.type = string;
	}
	public String createCode() {
//		MDJQCardModel card = new MDJQCardModel(name);
//		card.setTypes(this.getTypes());
		
		return null;
	}
	
}
