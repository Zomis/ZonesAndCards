package net.zomis.cards.crgame;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.zomis.utils.ZomisList;

public class CREffects {

	public static CREffect remove() {
		return (src, target) -> target.zoneMoveOnBottom(null);
	}
	
	public static CREffect createQuestionOfRandomQuality() {
		return (src, target) -> {
			Map<String, CRCardModel> cards = src.getGame().getCards();
			LinkedList<CRCardModel> zombies = ZomisList.filter2(cards.values(), model -> model.isZombie());
			src.getPlayer().getZombieZone().createCardOnBottom(ZomisList.getRandom(zombies, src.getGame().getRandom()));
		};
	}
	
	public static CREffect toRandom(CREffect effect, CRFilter filter) {
		return (src, target) -> {
			List<CRCard> all = src.getGame().findAll(src, filter);
			CRCard random = ZomisList.getRandom(all, src.getGame().getRandom());
			if (random != null)
				effect.apply(src, random);
		};
	}
	
	public static CREffect summonUser(String cardName, CREffect doWithUser) {
		return (src, dst) -> {
			CRCard card = src.getPlayer().getUserZone().createCardOnBottom(src.getGame().getCardModel(cardName));
			if (doWithUser != null)
				doWithUser.apply(src, card);
		};
	}

	public static CREffect summonZombie(String cardName) {
		return (src, dst) -> src.getPlayer().getUserZone().createCardOnBottom(src.getGame().getCardModel(cardName));
	}

}
