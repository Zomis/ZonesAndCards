package net.zomis.cards.wart;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.utils.ZomisUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WSWeaponCard extends WSBaseCard {
	public EnumSet<HSAbility> abilities = EnumSet.noneOf(HSAbility.class);
	
	@JsonCreator
	public WSWeaponCard(@JsonProperty("name") String name, @JsonProperty("manaCost") int manaCost) {
		super(name, manaCost);
	}

	public int atk;
	public int hp;
	
	@Override
	public String toString() {
		return getCardType() + " [atk=" + atk + ", hp=" + hp + ", " + super.toString() + "]";
	}
	
	@Override
	protected CardType getCardType() {
		return CardType.WEAPON;
	}

	public int parseDesc() {
		int result = 0;
		List<String> parsed = descs.stream().filter(this::parse).collect(Collectors.toList());
		result += parsed.size();
		descs.removeAll(parsed);
		result += super.parseDesc();
		return result;
	}

	private boolean parse(String str) {
		if (str.contains("Deathrattle"))
			return false;
		if (str.contains("Battlecry"))
			return false;
		
		EnumSet<HSAbility> supported = EnumSet.of(HSAbility.TAUNT, HSAbility.DIVINE_SHIELD, HSAbility.CHARGE, HSAbility.STEALTH, HSAbility.WINDFURY);
		if (str.startsWith("<b>") && str.endsWith("</b>")) {
			String ability = ZomisUtils.textBetween(str, "<b>", "</b>").toUpperCase();
			String[] abilityStrings = ability.split(",");
			for (String abString : abilityStrings) {
				String findAbility = abString.trim().replace(' ', '_').replace(".", "");
				if (!supported.stream().anyMatch(unsupport -> findAbility.toUpperCase().contains(unsupport.toString()))) {
					return false;
				}
				abilities.add(HSAbility.valueOf(findAbility));
			}
			return true;
		}
		
		return false;
	}
	
}
