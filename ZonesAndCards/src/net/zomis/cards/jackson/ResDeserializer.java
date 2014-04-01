package net.zomis.cards.jackson;

import java.io.IOException;

import net.zomis.cards.jackson.MixResource.ResourceSaveData;
import net.zomis.cards.jackson.MixResource.ResourceMapSave;
import net.zomis.cards.resources.ResourceMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ResDeserializer extends StdDeserializer<ResourceMap> {
	private static final long	serialVersionUID	= 3841807513680420079L;

	public ResDeserializer() {
		super(ResourceMap.class);
	}

	@Override
	public ResourceMap deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		ResourceMap map = new ResourceMap();
		ResourceMapSave data = parser.readValueAs(ResourceMapSave.class);
		for (ResourceSaveData ee : data.getData()) {
			map.set(ee.resource, ee.value);
			map.setListener(ee.resource, ee.listener);
			map.setResourceStrategy(ee.resource, ee.strategy);
		}
		return map;
	}
}
