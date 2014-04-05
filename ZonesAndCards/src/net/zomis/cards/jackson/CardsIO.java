package net.zomis.cards.jackson;

import java.io.IOException;

import net.zomis.cards.model.CardGame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CardsIO {

	public static ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		CardsModule module = new CardsModule();
		
		mapper.registerModule(module);
	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		return mapper;
	}

	public static <E> E load(String data, Class<E> clazz) {
		try {
			return mapper().reader(clazz).readValue(data);
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String save(CardGame<?, ?> game) {
		try {
			return mapper().writer().writeValueAsString(game);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
