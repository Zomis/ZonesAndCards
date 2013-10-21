package net.zomis.cards.jackson;

import java.io.IOException;

import net.zomis.cards.jackson.MixResource.ResourceMapSave;
import net.zomis.cards.util.ResourceMap;
import net.zomis.custommap.CustomFacade;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ResSerializer extends StdSerializer<ResourceMap> {

	public ResSerializer() {
		super(ResourceMap.class);
	}

	@Override
	public void serializeWithType(ResourceMap object, JsonGenerator jsongen, SerializerProvider serializer, TypeSerializer type) throws IOException, JsonProcessingException {
		CustomFacade.getLog().w("My Serializer used for " + object);
		ResourceMapSave data = new ResourceMapSave(object);
//		type.writeTypePrefixForObject(data, jsongen, ResourceMap.class);
		jsongen.writeObject(data);
	}

	@Override
	public void serialize(ResourceMap object, JsonGenerator jsongen, SerializerProvider serializer) throws IOException, JsonGenerationException {
//		CustomFacade.getLog().w("serialize method used");
		ResourceMapSave data = new ResourceMapSave(object);
		jsongen.writeObject(data);
	}
	
}
