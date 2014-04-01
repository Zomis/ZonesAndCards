package net.zomis.cards.jackson;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.ai.CardAI;
import net.zomis.cards.resources.ResourceMap;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CardsModule extends SimpleModule {

	private static final long	serialVersionUID	= -7169307056690905578L;

	public CardsModule() {
		super("CardsModule", new Version(1, 0, 0, null, "net.zomis", "cards"));
		this.addSerializer(ResourceMap.class, new ResSerializer());
		this.addDeserializer(ResourceMap.class, new ResDeserializer());
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(Card.class, MixCard.class);
		context.setMixInAnnotations(CardGame.class, MixGame.class);
		context.setMixInAnnotations(CardZone.class, MixZone.class);
		context.setMixInAnnotations(CardModel.class, MixCardModel.class);
		context.setMixInAnnotations(Player.class, MixPlayer.class);
		
		context.setMixInAnnotations(ResourceMap.class, MixResource.class);
		context.setMixInAnnotations(CardAI.class, MixAI.class);
//		context.setMixInAnnotations(UnmodifiableResource.class, MixUnmodifiable.class);
//		context.setMixInAnnotations(UnmodifiableResource.class, MixAddObjectId.class);
	}

}
