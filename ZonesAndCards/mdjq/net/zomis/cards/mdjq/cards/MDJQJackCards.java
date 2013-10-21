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
	
	public List<MDJQJacksonCard> list;
	
	@JsonIgnore
	private MDJQJacksonCard selected;
	
	@JsonIgnore
	public MDJQJacksonCard getSelected() {
		return selected;
	}

	void select(String cardName) {
		for (MDJQJacksonCard ac : list) {
			if (ac.name.matches(cardName)) {
				this.selected = ac;
				break;
			}
		}
	}
	
}
