package net.zomis.cards.crgame.ais;

import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.cards.crgame.CRCard;
import net.zomis.cards.crgame.CRPlayer;
import net.zomis.cards.model.ai.CardAIGeneric;

public class AI2 extends CardAIGeneric<CRPlayer, CRCard> {
	
	public AI2() {
		// TODO: AI Problem: How to handle target decisions in a smooth way? Would it be easier to handle both source and target at once??
		// TODO: Generics problem: CardAI is not a generic type. Need to create ScoreConfig<Player, Card<?>> which will involve a lot of typecasting
		// TODO: If CardAI is a generic type, then how to solve it in Player, which has a CardAI property?
//		ScoreConfigFactory<CRPlayer, CRCard> config = new ScoreConfigFactory<>();
		ScoreConfigFactory<CRPlayer, CRCard> config = new ScoreConfigFactory<>();
//		FScorer8<Player, Card<?>> cc = this.removeOpponentUser();
//		FScorer8<CRPlayer, CRCard> cc = new FScorer8<CRPlayer, CRCard>() {
//
//			@Override
//			public double getScoreFor(CRCard field, ScoreParameters<CRPlayer> scores) {
//				CRCard cr = (CRCard) field;
//				if (cr.getModel().getName().equals("End Turn"))
//					return -0.4;
//				if (cr.getModel().getEffect() == null)
//					return 0.3;
//				if (cr.getModel().getEffect().getClass() == new CREffects().summonUser(null, null).getClass()) {
//					System.out.println("YES! " + field);
//				}
//				return cr.getModel().getEffect().getClass() == new CREffects().summonUser(null, null).getClass() ? 1 : 0;
//			}
//		};
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
