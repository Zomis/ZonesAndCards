package net.zomis.cards.jackson;

import java.io.IOException;

import net.zomis.cards.jackson.MixResource.ResourceMapSave;
import net.zomis.cards.resources.ResourceMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ResSerializer extends StdSerializer<ResourceMap> {

	public ResSerializer() {
		super(ResourceMap.class);
	}

	@Override
	public void serialize(ResourceMap object, JsonGenerator jsongen, SerializerProvider serializer) throws IOException, JsonGenerationException {
//		CustomFacade.getLog().i("serialize method used");
		ResourceMapSave data = new ResourceMapSave(object);
		jsongen.writeObject(data);
	}
	
}
