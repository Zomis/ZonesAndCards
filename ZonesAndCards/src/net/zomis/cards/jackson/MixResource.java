package net.zomis.cards.jackson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceListener;
import net.zomis.cards.util.ResourceMap;
import net.zomis.cards.util.ResourceStrategy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonSerialize(using=ResSerializer.class)
@JsonDeserialize(using=ResDeserializer.class)
public abstract class MixResource {
	
	public static final class ResourceMapSave {
		@JsonProperty
		private final ArrayList<ResourceData> data;
		private final Map<IResource, ResourceData> resources;
		public ResourceMapSave() {
			this.data = null;
			this.resources = null;
		}
		public ResourceMapSave(ResourceMap map) {
			this.data = new ArrayList<ResourceData>();
			this.resources = new HashMap<IResource, MixResource.ResourceData>();
			
			for (Entry<IResource, Integer> ee : map.getValues()) {
				dataFor(ee.getKey()).value = ee.getValue();
			}
			for (Entry<IResource, ResourceListener> ee : map.getListeners().entrySet()) {
				dataFor(ee.getKey()).listener = ee.getValue();
			}
			for (Entry<IResource, ResourceStrategy> ee : map.getStrategies().entrySet()) {
				dataFor(ee.getKey()).strategy = ee.getValue();
			}
		}
		private ResourceData dataFor(IResource key) {
			ResourceData map = resources.get(key);
			if (map == null) {
				map = new ResourceData(key);
				resources.put(key, map);
				data.ensureCapacity(data.size() + 1);
				data.add(map);
			}
			return map;
		}
		public List<ResourceData> getData() {
			return data;
		}
	}
	
	public static class ResourceData {
		public ResourceData() {}
		public ResourceData(IResource key) {
			this.resource = key;
		}
		public IResource resource;
		public ResourceStrategy strategy;
		public ResourceListener listener;
		public Integer value;
	}
	
	
}
