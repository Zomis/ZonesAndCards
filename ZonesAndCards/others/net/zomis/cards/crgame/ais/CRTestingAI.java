package net.zomis.cards.crgame.ais;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.CardAI;

public class CRTestingAI extends CardAI {
	
	public CRTestingAI() {
		// TODO: AI Problem: How to handle target decisions in a smooth way? Would it be easier to handle both source and target at once??
		// TODO: Generics problem: CardAI is not a generic type. Need to create ScoreConfig<Player, Card<?>> which will involve a lot of typecasting
		// TODO: If CardAI is a generic type, then how to solve it in Player, which has a CardAI property?
//		ScoreConfigFactory<CRPlayer, CRCard> config = new ScoreConfigFactory<>();
		ScoreConfigFactory<Player, Card<?>> config = new ScoreConfigFactory<>();
//		FScorer8<Player, Card<?>> cc = this.removeOpponentUser();
//		config.withScorer(cc);
		
		this.setConfig(config);
		
	}

//	public FScorer8<Player, Card<?>> removeOpponentUser() {
//		return (card, params) -> {
//			return 0;
////			CRCard cr = (CRCard) card;
////			if (cr.getModel().getEffect().getClass() == new CREffects().summonUser(null, null).getClass()) {
////				System.out.println("YES! " + card);
////			}
////			return cr.getModel().getEffect().getClass() == new CREffects().summonUser(null, null).getClass() ? 1 : 0;
//		};
//	}
	
}
