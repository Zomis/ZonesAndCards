package net.zomis.cards.mdjq.cards;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


@JacksonXmlRootElement(localName="cards")
public class MDJQJackCards {

	public String name;
	public String key;
	public String comment;
	public String type;
	
	public List<MDJQJackCard> list;
	
	@JsonIgnore
	private MDJQJackCard selected;
	
	@JsonIgnore
	public MDJQJackCard getSelected() {
		return selected;
	}

	void select(String cardName) {
		for (MDJQJackCard ac : list) {
			if (ac.name.matches(cardName)) {
				this.selected = ac;
				break;
			}
		}
	}
	
}
