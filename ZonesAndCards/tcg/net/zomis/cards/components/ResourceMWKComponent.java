package net.zomis.cards.components;

import net.zomis.cards.iface.Component;
import net.zomis.cards.resources.IResource;
import net.zomis.cards.resources.ResourceData;
import net.zomis.cards.resources.ResourceMap;


public class ResourceMWKComponent implements Component {

	private enum Res implements IResource {
		MAGES, WARRIORS, KINGS;

		@Override
		public ResourceData createData(IResource resource) {
			return ResourceData.forResource(resource);
		}
	}
	
	private final ResourceMap	resMap;

	public ResourceMWKComponent(ResourceMap resourceMap, int mages, int warriors, int kings) {
		this.resMap = resourceMap;
		setMages(mages);
		setWarriors(warriors);
		setKings(kings);
	}
	
	public int getKings() {
		return resMap.get(Res.KINGS);
	}
	
	public int getMages() {
		return resMap.get(Res.MAGES);
	}
	
	public int getWarriors() {
		return resMap.get(Res.WARRIORS);
	}
	
	public void setKings(int kings) {
		resMap.set(Res.KINGS, kings);
	}
	
	public void setMages(int mages) {
		resMap.set(Res.MAGES, mages);
	}
	
	public void setWarriors(int warriors) {
		resMap.set(Res.WARRIORS, warriors);
	}

	public boolean has(int mages, int warriors, int kings) {
		return getMages() >= mages && getWarriors() >= warriors && getKings() >= kings;
	}

	public void change(int mages, int warriors, int kings) {
		resMap.changeResources(Res.MAGES, mages);
		resMap.changeResources(Res.WARRIORS, warriors);
		resMap.changeResources(Res.KINGS, kings);
	}
	
	@Override
	public String toString() {
		return resMap.toString();
	}
	
	
}
