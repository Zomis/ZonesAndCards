package net.zomis.cards.crgame;

import static net.zomis.cards.crgame.CREffects.*;
import static net.zomis.cards.crgame.CRFilters.*;

public enum CRCards implements CardEnum<CRCardModel> {

	GIVE_ME_DA_CODEZZZ(zombie("give me da codezzz", -5)),
	EVENT_HANDLER_WITH_LAMBDAS(zombie("Event Handler with lambdas", 4)),
	YOU_ABOUT_YOU(zombie("Questions and Answers: Let me tell you about you", 10)),
	LINKED_LIST(zombie("Linked List in yet another way", -3)),
	
	// CR Users:
	JAMAL(user("Jamal", 3).giveHours(2)),
	ROLFL(user("Rolfl", 3).giveHours(2)),
	SIMON(user("Simon André Forsberg", 3).giveHours(2)),
	MUG(user("Mat's Mug", 3).giveHours(2)),
	THE_FISH(user("200_success", 3).giveHours(2)),
	KONIJN(user("konijn", 3).giveHours(2)),
	
	// PCG Users:
	DOORKNOB(user("Doorknob", 3).giveHours(2)),
	
	// Random users:
	USER(user("userXXXXXXX", 1).giveHours(1)),
	
	CLOSE(spell("CVA", 3).desc("Close Vote Applied. Close a question").toTarget(remove(), isZombie())),
	EXPLODING_BEAR_TRAP(spell("Exploding Bear Trap", 1).desc("Place an exploding bear trap on the enemy site, removing a user that has been there for less than 3 turns.")
			.effect(toRandom(remove(), opponent().and(isUser()).and(ageLess(3))))),
	BTW_WORK(spell("BTW.Work", 4).effect(toRandom(remove(), opponent().and(isUser())))),
	
	CROLATDMS(spell("CROLATDMS", 3).desc("Completely Ridiculous Overly Long Acronym That Doesn't Make Sense: Create confusion and decrease opponent site quality").effect(oppQuality(-50))),
	
	TTQW(spell("TTQW", 4).effect(toRandom(remove(), opponent().and(isUser())))),
	TTGTB(spell("TTGTB", 4).effect(toRandom(remove(), opponent().and(isUser())))),
	ASK_A_QUESTION(spell("Ask a question", 1).desc("Ask a question. Can be anything").effect(createQuestionOfRandomQuality())),
	JAMALIZE(spell("Jamalize", 1).desc("Jamalize a question and improve it's quality").toTarget(increaseQuality(2), mine().and(isZombie()))),
	MONKING(spell("Monking", 1).desc("A user joins").effect(summonUser(USER.cardName(), setRandomUserName()))),
	PIMP_QUESTION(spell("Pimp Question", 2).toTarget(increaseQuality(1), mine().and(isZombie()).and(qualityMore(0)))),
	TAG_NOOOOOOO(spell("tag:noooooooooooooo", 4).desc("Create yet another linked list question").effect(CRCards::createLinkedListQuestion)),
	THE_MISSION(spell("THE MISSION", 20).desc("Drastically increase site quality").effect(quality(200))),
	
	;
	
	private final CRCardModel	model;
	
	public static void createLinkedListQuestion(CRCard source, CRCard target) {
		source.getPlayer().getNextPlayer().getZombieZone().createCardOnBottom(LINKED_LIST.model);
	}

	private static CREffect oppQuality(int i) {
		return (src, dst) -> src.getPlayer().getNextPlayer().getResources().changeResources(CRRes.QUALITY, i);
	}

	private static CREffect quality(int i) {
		return (src, dst) -> src.getPlayer().getResources().changeResources(CRRes.QUALITY, i);
	}

	@Override
	public CRCardModel getModel() {
		return model;
	}
	
	private static CREffect setRandomUserName() {
		return (src, dst) -> {
			int random = src.getGame().getRandom().nextInt(9_000_000) + 1_000_000;
			dst.setName("user" + random);
		};
	}

	private static CRFilter qualityMore(int i) {
		return (src, dst) -> dst.getQuality() >= i;
	}

	public static CREffect increaseQuality(int i) {
		return (src, dst) -> dst.changeQuality(i);
	}

	private static CRCardModel user(String name, int i) {
		return new CRCardModel(name, CRCardType.USER, i);
	}

	private CRCards(CRCardModel model) {
		this.model = model;
	}
	
	private static CRCardModel zombie(String string, int i) {
		return new CRCardModel(string, CRCardType.ZOMBIE, 0).quality(i);
	}

	private static CRCardModel spell(String string, int i) {
		return new CRCardModel(string, CRCardType.SPELL, i);
	}

	public static void createCards(CRCardGame crCardGame) {
		for (CRCards card : CRCards.values()) {
			crCardGame.addCard(card.model);
		}
	}

	public String cardName() {
		return model.getName();
	}

	
	
	
}
