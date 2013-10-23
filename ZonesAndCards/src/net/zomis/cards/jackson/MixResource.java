package net.zomis.cards.jackson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.zomis.cards.util.IResource;
import net.zomis.cards.util.ResourceData;
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
		private final ArrayList<ResourceSaveData> data;
		private final Map<IResource, ResourceSaveData> resources;
		public ResourceMapSave() {
			this.data = null;
			this.resources = null;
		}
		public ResourceMapSave(ResourceMap map) {
			this.data = new ArrayList<ResourceSaveData>();
			this.resources = new HashMap<IResource, MixResource.ResourceSaveData>();
			
			for (Entry<IResource, ResourceData> ee : map.getData().entrySet()) {
				ResourceSaveData save = dataFor(ee.getKey());
				save.listener = ee.getValue().getListener();
				save.resource = ee.getValue().getResource();
				save.value = ee.getValue().getRealValue();
				save.strategy = ee.getValue().getStrategy();
			}
		}
		private ResourceSaveData dataFor(IResource key) {
			ResourceSaveData map = resources.get(key);
			if (map == null) {
				map = new ResourceSaveData(key);
				resources.put(key, map);
				data.ensureCapacity(data.size() + 1);
				data.add(map);
			}
			return map;
		}
		public List<ResourceSaveData> getData() {
			return data;
		}
	}
	
	public static class ResourceSaveData {
		public ResourceSaveData() {}
		public ResourceSaveData(IResource key) {
			this.resource = key;
		}
		public IResource resource;
		public ResourceStrategy strategy;
		public ResourceListener listener;
		public Integer value;
	}
	
	
}
