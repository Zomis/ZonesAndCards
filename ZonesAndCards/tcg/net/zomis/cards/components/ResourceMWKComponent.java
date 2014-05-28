package net.zomis.cards.components;


public class ResourceMWKComponent implements PlayerComponent {

	private int mages, warriors, kings;

	public ResourceMWKComponent(int mages, int warriors, int kings) {
		this.mages = mages;
		this.warriors = warriors;
		this.kings = kings;
	}
	
	public int getKings() {
		return kings;
	}
	
	public int getMages() {
		return mages;
	}
	
	public int getWarriors() {
		return warriors;
	}
	
	public void setKings(int kings) {
		this.kings = kings;
	}
	
	public void setMages(int mages) {
		this.mages = mages;
	}
	
	public void setWarriors(int warrios) {
		this.warriors = warrios;
	}

	public boolean has(int mages, int warriors, int kings) {
		return this.mages >= mages && this.warriors >= warriors && this.kings >= kings;
	}

	public void change(int mages, int warriors, int kings) {
		this.mages += mages;
		this.warriors += warriors;
		this.kings += kings;
	}
	
	@Override
	public String toString() {
		return "{" + mages + ", " + warriors + ", " + kings + "}";
	}
	
	
}
