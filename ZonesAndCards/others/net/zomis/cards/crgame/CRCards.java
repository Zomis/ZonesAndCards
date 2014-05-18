package net.zomis.cards.crgame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CRCards {
	// GWT seem to not be able to handle lambdas in a static context, so for now I'm creating an instance of the classes holding lambda methods... but why does it work for this class? there's static methods below using lambdas...
	
	private static final CRFilters f = new CRFilters();
	private static final CREffects ef = new CREffects();
	
	// Weekend challenge:
	public static final CRCardModel SUDOKU_SHARP = zombie("SudokuSharp Solver with advanced features", 8);
	public static final CRCardModel SUDOKU_PYTHON = zombie("How Pythonic is my Sudoku Solver?", 4);
	public static final CRCardModel SUDOKU_RUBY = zombie("Ruby Sudoku solver", 4);
	public static final CRCardModel SUDOKU_JAVA = zombie("Hello Java World ~> Parsing a Sudoku Grid", 4);
	
	public static final CRCardModel TTGTB = spell("TTGTB", 5).desc("Remove all hours on opponent site").effect(ef::removeOppHours);
	
	public static final CRCardModel UTTT_JAVA = zombie("Recursive approach to Tic-Tac-Toe", 4);
	public static final CRCardModel UTTT_C = zombie("Ultimate Tic-Tac-Toe in C", 9);
	public static final CRCardModel UTTT_AS3 = zombie("UltimateTicTacToe - ActionScript Style!", 5);
	public static final CRCardModel UTTT_JAVA_WINNER = zombie("Tic-tac-toe 'get winner' algorithm", 7);
	
	public static final CRCardModel[] CHALLENGE_SUDOKU = { SUDOKU_JAVA, SUDOKU_PYTHON, SUDOKU_RUBY, SUDOKU_SHARP };
	public static final CRCardModel[] CHALLENGE_UTTT = { UTTT_AS3, UTTT_C, UTTT_JAVA, UTTT_JAVA_WINNER };
	
	
	public static final CRCardModel GIVE_ME_DA_CODEZZZ = zombie("give me da codezzz", -3);
	public static final CRCardModel EVENT_HANDLER_WITH_LAMBDAS = zombie("Event Handler with lambdas", 4);
	public static final CRCardModel YOU_ABOUT_YOU = zombie("Questions and Answers: Let me tell you about you", 5);
	public static final CRCardModel LINKED_LIST = zombie("Linked List in yet another way", -2);
	
	// CR Users:
	public static final CRCardModel JAMAL = user("Jamal", 4).giveHours(4);
	public static final CRCardModel ROLFL = user("rolfl", 4).giveHours(4);
	public static final CRCardModel THE_FISH = user("200_success", 4).giveHours(4);
	
	public static final CRCardModel SIMON = user("Simon Andre Forsberg", 3).giveHours(2);
	public static final CRCardModel MUG = user("Mat's Mug", 3).giveHours(2);
	public static final CRCardModel KONIJN = user("konijn", 3).giveHours(2);
	
	// PCG Users:
	public static final CRCardModel DOORKNOB = user("Doorknob", 3).giveHours(2);
	public static final CRCardModel GROVES_NL = user("grovesNL", 3).giveHours(2);
	public static final CRCardModel THE_DOCTOR = user("TheDoctor", 3).giveHours(2);
	
	// Random users:
	public static final CRCardModel USER = user("userXXXXXXX", 1).giveHours(1);
	
	public static final CRCardModel CLOSE = spell("CVA", 3).desc("Close Vote Applied. Close a question").toTarget(ef.remove(), f.isZombie());
	public static final CRCardModel CLOSE_POLICE = spell("Close Police", 5).desc("Close all bad questions on your site").effect(ef.toAll(ef.remove(), f.isZombie().and(f.isBad())));
	public static final CRCardModel EXPLODING_BEAR_TRAP = spell("Exploding Bear Trap", 0).desc("Place an exploding bear trap on the enemy site, removing all users with age <= 2")
			.effect(ef.toAll(ef.remove(), f.opponent().and(f.isUser()).and(f.ageLess(2))));
	
	
	public static final CRCardModel ROLATDMS = spell("ROLATDMS", 5).desc("Ridiculous Overly Long Acronym That Doesn't Make Sense: Decrease opponent site quality").effect(ef.oppQuality(-20));
	
	public static final CRCardModel ASK_A_QUESTION = spell("Ask a question", 1).desc("Ask a question. Can be anything").effect(ef.createQuestionOfRandomQuality());
	public static final CRCardModel JAMALIZE = spell("Jamalize", 1).desc("Jamalize a question and improve it's quality").toTarget(ef.increaseQuality(5), f.mine().and(f.isZombie()));
	public static final CRCardModel MONKING = spell("Monking" , 1).desc("A new user joins").effect(ef.summonUser(USER.getName(), ef.setRandomUserName()));
	public static final CRCardModel WELCOME = spell("Welcome!", 1).desc("A new user joins").effect(ef.summonUser(USER.getName(), ef.setRandomUserName()));
	public static final CRCardModel PIMP_QUESTION = spell("Pimp Question", 2).desc("Boost a question quality that's already positive").toTarget(ef.increaseQuality(2), f.mine().and(f.isZombie()).and(f.qualityMore(0)));
	public static final CRCardModel TAG_NOOOOOOO = spell("tag:noooooo", 4).desc("Create a linked list question on the opponent site").effect(ef::createLinkedListQuestion);
	public static final CRCardModel THE_MISSION = spell("THE MISSION", 20).desc("Drastically increase site quality").effect(ef.quality(42));
	
	public static final CRCardModel BTW_WORK = spell("BTW.Work", 4).desc("Send a random opponent user back to work").effect(ef.toRandom(ef.remove(), f.opponent().and(f.isUser())));
	public static final CRCardModel TTQW = spell("TTQW", 5).desc("A user on opponent site quits work. Don't stack-and-drive").effect(ef.toRandom(ef.remove(), f.opponent().and(f.isUser())));
	
	public static final CRCardModel HOT_QUESTION = spell("Hot Question", 5).desc("A question on your site attracts lots of attention, draw 4 cards").effect(ef.drawCards(4));
	public static final CRCardModel WEEKEND_CHALLENGE = spell("Weekend Challenge", 4).desc("Create some Weekend Challenge questions").effect(ef.weekendChallenge());
	
	public static final Set<CRCardModel> crCards = new HashSet<>(Arrays.asList(JAMAL, ROLFL, SIMON, MUG, THE_FISH, KONIJN, 
			THE_MISSION, JAMALIZE, PIMP_QUESTION, WEEKEND_CHALLENGE, USER, TTGTB));
	public static final Set<CRCardModel> pcgCards = new HashSet<>(Arrays.asList(DOORKNOB, GROVES_NL, THE_DOCTOR, USER));
	
	private static CRCardModel user(String name, int i) {
		return new CRCardModel(name, CRCardType.USER, i);
	}

	private static CRCardModel zombie(String string, int i) {
		return new CRCardModel(string, CRCardType.ZOMBIE, 0).quality(i);
	}

	private static CRCardModel spell(String string, int i) {
		return new CRCardModel(string, CRCardType.SPELL, i);
	}

	public static void createCards(CRCardGame crCardGame) {
		List<CRCardModel> arr = Arrays.asList(new CRCardModel[]{ GIVE_ME_DA_CODEZZZ, EVENT_HANDLER_WITH_LAMBDAS, YOU_ABOUT_YOU, LINKED_LIST,
				JAMAL, ROLFL, SIMON, MUG, THE_FISH, KONIJN,
				DOORKNOB,
				USER,
					HOT_QUESTION, WEEKEND_CHALLENGE, CLOSE_POLICE, SUDOKU_JAVA, SUDOKU_PYTHON, SUDOKU_RUBY, SUDOKU_SHARP, UTTT_AS3, UTTT_C, UTTT_JAVA, UTTT_JAVA_WINNER,
					TTGTB,
				CLOSE, EXPLODING_BEAR_TRAP, BTW_WORK, WELCOME,
		
				ROLATDMS, TTQW, ASK_A_QUESTION, JAMALIZE, MONKING, PIMP_QUESTION, TAG_NOOOOOOO, THE_MISSION });
//		for (CRCards cards : CRCards.values()) {
//			crCardGame.addCard(cards.model);
//		}
		for (CRCardModel model : arr) {
			crCardGame.addCard(model);
		}
		
		
	}

}
