package net.zomis.cards.wart;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.zomis.cards.wart.HStoneClass;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.factory.CardType;
import net.zomis.cards.wart.factory.HSAbility;
import net.zomis.cards.wart.factory.HStoneChars;
import net.zomis.custommap.ZomisSwing;
import net.zomis.utils.ZomisUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WSLoad {

	private List<WSBaseCard>	cards;

	public WSLoad(List<WSBaseCard> cards) {
		this.cards = cards;
		
		cards.stream().filter(card -> card.name.toLowerCase().equals("unleash the hounds")).findAny().get().cost++;
		
		// Remove tailing dots in card texts
		cards.forEach(card -> card.descs.replaceAll(str -> cleardots(str)));
		
	}

	public static void main(String[] args) {
		ZomisSwing.setup();
		try {
			System.out.println("------------- Minions");
			File f = new File("C:/Python34/_dat/hstone");
			WSMinionCard[] minions = new ObjectMapper().readValue(new File(f, "minions.json"), WSMinionCard[].class);
//			Arrays.stream(minions).forEach(System.out::println);
			
			System.out.println("------------- Spells");
			WSSpellCard[] spells = new ObjectMapper().readValue(new File(f, "spells.json"), WSSpellCard[].class);
//			Arrays.stream(spells).forEach(System.out::println);
			
			System.out.println("------------- Weapons");
			WSWeaponCard[] weapons = new ObjectMapper().readValue(new File(f, "weapons.json"), WSWeaponCard[].class);
//			Arrays.stream(weapons).forEach(System.out::println);
			
			System.out.println("---------------- ALL");
			
			List<WSBaseCard> cards = new ArrayList<>();
			cards.addAll(Arrays.asList(minions));
			cards.addAll(Arrays.asList(spells));
			cards.addAll(Arrays.asList(weapons));
			
			WSLoad loader = new WSLoad(cards);
			loader.run();
			loader.newParse();
			loader.showUnimplemented();
			loader.showCards("Feral Spirit, Lightning Storm, Arcane Golem");
			testCards(cards);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showCards(String string) {
		String[] optionsS = string.split(", ");
		Set<String> options = new HashSet<>(Arrays.asList(optionsS));
		options.stream().map(name -> cards.stream().filter(card -> card.name.equals(name)).findAny().get()).forEach(System.out::println);
	}

	private void showUnimplemented() {
		HStoneGame game = new HStoneGame(HStoneChars.jaina("Player1"), HStoneChars.jaina("Player2"));
		
		List<WSBaseCard> unimplemented = cards.stream().filter(card -> game.getCardModel(card.name) == null).collect(Collectors.toList());
		System.out.println("--- Unimplemented Cards: " + unimplemented.size() + " ---");
		unimplemented.forEach(System.out::println);
		
		System.out.println("Implemented Cards that shouldn't exist:");
		game.getCards().keySet().stream().filter(cardModel -> cards.stream().noneMatch(card -> card.name.equals(cardModel))).forEach(System.out::println);
	}

	private void newParse() {
		replace("\\s+", " ");
		replace("a friendly minion", "{WHO/friendlyMinion}");
		replace("Destroy", "{WHAT/destroy}");
		replace("Deal (\\d+) damage", "{WHAT/damage($1)}");
		replace(", ", " ");
		replace("<i>[^<]+</i>", "");
		replace("<b>Deathrattle:</b>(.*)", "deathrattle($1)");
		replace("Draw a card", "drawCard()");
		replace("to ALL characters", "forEach(all(), null, {ACTION})");
		replace("Gain (\\d+) Armor", "{Armor:$1}");
		replace("Overload:</b> \\((\\d+)\\)", "{Overload:$1}");
		replace("</?b>", "");
		replace("(Windfury|Taunt|Divine Shield|Charge)", "{Ability:$1}");
		replace("[\\s,]*\\]", "]");
		System.out.println("New cards:");
		cards.stream().filter(card -> !card.descs.isEmpty()).map(card -> card.fullDesc).forEach(System.out::println);
		
	}

	private void replace(String string, String string2) {
		cards.stream().forEach(card -> card.descs.replaceAll(str -> str.replaceAll(string, string2)));
		cards.stream().forEach(card -> card.fullDesc = card.fullDesc.replaceAll(string, string2));
	}

	private void run() {
		long count = cards.stream().filter(card -> card.descs.isEmpty()).count();
		System.out.println(count + " / " + cards.size() + " cards has no special text");
		System.out.println("All descs are " + cards.stream().collect(Collectors.summarizingInt(card -> card.descs.size())));
		findAndPrint("Windfury");
//		cards.stream().forEach(card -> card.parseDesc());
		System.out.println("After parse:  " + cards.stream().collect(Collectors.summarizingInt(card -> card.descs.size())));
		System.out.println("Cards with text:");
		cards.stream().filter(card -> !card.descs.isEmpty()).forEach(System.out::println);
		
		System.out.println("----------");
		cards.stream().flatMap(card -> card.descs.stream()).filter(str -> str.contains(".")).forEach(System.out::println);
		System.out.println("--------------");
		
		findAndPrint("Destroy all enemy");
		findAndPrint("Look at the top three cards of your deck. Draw one and discard the others");
		findAndPrint("instead");
		findAndPrint("If");
		findAndPrint("Choose");
		findAndPrint("all enemy minions");
		findAndPrint("less");
		findAndPrint("it dies");
		findAndPrint("can't be");
		findAndPrint("deal it");

		print("Minions with stealth", str -> str.factory.card().getAbilities().contains(HSAbility.STEALTH));
		
//		final HStoneClass clazz = HStoneClass.WARRIOR;
//		print(clazz.toString(), str -> str.clazz == clazz);
		cards.stream()
			.filter(card -> card instanceof WSSpellCard)
			.filter(card -> card.clazz == HStoneClass.WARRIOR)
//			.map(card -> cardToMinionType(card))
			.peek(System.out::println)
			.collect(Collectors.toList());
		
	}
	String cardToMinionType(WSBaseCard card) {
		WSMinionCard minion = (WSMinionCard) card;
		return "\t\tfixMinionType(game, \"" + card.name + "\", HStoneMinionType." + minion.subtype + ");";
	}

	String cardToSetClass(WSBaseCard card) {
		return "\t\tfixCardClass(game, \"" + card.name + "\", HStoneClass." + card.clazz + ");";
	}

	String cardToStringFactory(WSBaseCard card) {
//		game.addCard(minion(2, FREE  , 3, 2, "Acidic Swamp Ooze").battlecry(destroyOppWeapon()).card());
//		game.addCard(spell(1, COMMON, "Ice Lance").effect(freezeOrDamage(4)).card());
		StringBuilder str = new StringBuilder("\t\tgame.addCard(");
		if (card.getCardType() == CardType.MINION) {
			WSMinionCard minion = (WSMinionCard) card;
			str.append(String.format("minion(%2d, %9s, %d, %d, \"%s\")", card.cost, card.rarity, minion.atk, minion.hp, card.name));
			card.descs.forEach(s -> str.append(".effect(\"" + s + "\")"));
		}
		if (card.getCardType() == CardType.SPELL) {
			str.append(String.format("spell(%2d, %9s, \"%s\")", card.cost, card.rarity, card.name));
			card.descs.forEach(s -> str.append(".effect(\"" + s + "\")"));
		}
		if (card.getCardType() == CardType.WEAPON) {
			WSWeaponCard weapon = (WSWeaponCard) card;
			str.append(String.format("weapon(%2d, %9s, %d, %d, \"%s\")", card.cost, card.rarity, weapon.atk, weapon.hp, card.name));
			card.descs.forEach(s -> str.append(".effect(\"" + s + "\")"));
		}
		if (card.clazz != null)
			str.append(".forClass(" + card.clazz.getClass().getSimpleName() + "." + card.clazz.name() + ")");
		str.append(".card());");
		return str.toString();
	}

	private void print(String title, Predicate<WSBaseCard> condition) {
		System.out.println("--------- Print: " + title);
		cards.stream().filter(condition).forEach(System.out::println);
	}

	private void findAndPrint(String string) {
		System.out.println("---------------");
		System.out.println("Search for: " + string);
		cards.stream().filter(text(string)).forEach(System.out::println);
	}

	private static Predicate<? super WSBaseCard> text(String string) {
		String search = string.toLowerCase();
		return card -> card.descs.stream().anyMatch(str -> str.toLowerCase().contains(search));
	}

	private static void testCards(List<WSBaseCard> cards) {
		WSWeaponCard doomhammer = cards.stream().filter(name("Doomhammer")).map(to(WSWeaponCard.class)).findAny().get();
		assertTrue(doomhammer.abilities.contains(HSAbility.WINDFURY));
//		assertTrue(doomhammer.abilities.contains(HSAbility.WINDFURY));
		
		WSMinionCard houndmaster = cards.stream().filter(name("Houndmaster")).map(to(WSMinionCard.class)).findAny().get();
		assertFalse(houndmaster.abilities.contains(HSAbility.TAUNT));
		
		
//		houndmaster.descs.replaceAll(arg0);
	}

	private static <T, R> Function<T, R> to(Class<R> class1) {
		return obj -> class1.cast(obj);
	}

	private static Predicate<? super WSBaseCard> name(String string) {
		return card -> card.name.equals(string);
	}
	
	public static String cleardots(String str) {
		if (str.startsWith("."))
			str = ZomisUtils.substr(str, 1);
		if (str.endsWith("."))
			str = ZomisUtils.substr(str, 0, -1);
		return str.trim();
	}
}
