package net.zomis.cards.crgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.zomis.utils.ZomisList;

public class CREffects {

	public CREffect remove() {
		return (src, target) -> target.destroy();
	}
	
	public CREffect toRandom(CREffect effect, CRFilter filter) {
		return (src, target) -> {
			List<CRCard> all = src.getGame().findAll(src, filter);
			CRCard random = ZomisList.getRandom(all, src.getGame().getRandom());
			if (random != null)
				effect.apply(src, random);
		};
	}
	
	public CREffect summonUser(String cardName, CREffect doWithUser) {
		return (src, dst) -> {
			CRCard card = src.getPlayer().getUserZone().createCardOnBottom(src.getGame().getCardModel(cardName));
			if (doWithUser != null)
				doWithUser.apply(src, card);
		};
	}

	public CREffect summonZombie(String cardName) {
		return (src, dst) -> src.getPlayer().getUserZone().createCardOnBottom(src.getGame().getCardModel(cardName));
	}

	public void createLinkedListQuestion(CRCard source, CRCard target) {
		createQuestion(source.getPlayer().getNextPlayer(), CRCards.LINKED_LIST);
	}
	
	public void removeOppHours(CRCard source, CRCard target) {
		source.getPlayer().getNextPlayer().getResources().set(CRRes.HOURS_AVAILABLE, 0);
		
	}

	public CREffect toAll(CREffect effect, CRFilter filter) {
		return new CREffect() {
			@Override
			public void apply(CRCard source, CRCard target) {
				List<CRCard> all = source.getGame().findAll(source, filter);
				for (CRCard card : all) {
					effect.apply(source, card);
				}
			}
		};
	}

	public CREffect weekendChallenge() {
		return new CREffect() {
			@Override
			public void apply(CRCard source, CRCard target) {
				Random random = source.getGame().getRandom();
				CRCardModel[][] choices = { CRCards.CHALLENGE_SUDOKU, CRCards.CHALLENGE_UTTT };
				List<CRCardModel> challenge = new ArrayList<CRCardModel>(Arrays.asList(ZomisList.getRandom(choices, random)));
				int count = random.nextInt(challenge.size()) + 1;
				for (int i = 0; i < count; i++) {
					CRCardModel question = ZomisList.getRandom(challenge, random);
					challenge.remove(question);
					createQuestion(source.getPlayer(), question);
				}
			}
		};
	}

	public CREffect drawCards(int i) {
		return (src, dst) -> src.getPlayer().drawCards(i);
	}

	public CREffect oppQuality(int i) {
		return (src, dst) -> src.getPlayer().getNextPlayer().getResources().changeResources(CRRes.QUALITY, i);
	}

	public CREffect quality(int i) {
		return (src, dst) -> src.getPlayer().getResources().changeResources(CRRes.QUALITY, i);
	}

	public CREffect setRandomUserName() {
		return (src, dst) -> {
			int random = src.getGame().getRandom().nextInt(9_000_000) + 1_000_000;
			dst.setName("user" + random);
		};
	}

	public CREffect increaseQuality(int i) {
		return (src, dst) -> dst.changeQuality(i);
	}

	public CREffect createQuestionOfRandomQuality() {
		return (src, target) -> {
			CRCardModel[] zombies = { CRCards.GIVE_ME_DA_CODEZZZ, CRCards.EVENT_HANDLER_WITH_LAMBDAS, CRCards.YOU_ABOUT_YOU  };
			createQuestion(src.getPlayer(), ZomisList.getRandom(zombies, src.getGame().getRandom()));
		};
	}

	private void createQuestion(CRPlayer player, CRCardModel zombie) {
		if (!zombie.isZombie())
			throw new IllegalArgumentException();
		player.getZombieZone().createCardOnBottom(zombie);
	}

	public CREffect createGolfQuestion() {
		return (src, dst) -> {
			CRCardModel[] zombies = { CRCards.GOLF_CEPTION, CRCards.GOLF_PALINDROME, CRCards.GOLF_PLUS, CRCards.GOLF_SHORTER };
			createQuestion(src.getPlayer(), ZomisList.getRandom(zombies, src.getGame().getRandom()));
		};
	}
}
