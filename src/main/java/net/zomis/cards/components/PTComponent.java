package net.zomis.cards.components;

import net.zomis.cards.iface.Component;


public class PTComponent implements Component {

	private int power;
	private int thoughness;
	private int maxThoughness;

	public PTComponent(int power, int thoughness) {
		setPower(power);
		setThoughness(thoughness);
		setMaxThoughness(thoughness);
	}
	
	public int getPower() {
		return power;
	}
	
	public int getThoughness() {
		return thoughness;
	}
	
	public int getMaxThoughness() {
		return maxThoughness;
	}
	
	public boolean isAlive() {
		return thoughness > maxThoughness;
	}
	
	public void damage(int i) {
		thoughness -= i;
	}
	
	public void heal(int i) {
		thoughness += i;
		if (thoughness > maxThoughness) {
			thoughness = maxThoughness;
		}
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public void setThoughness(int thoughness) {
		this.thoughness = thoughness;
	}
	
	public void setMaxThoughness(int maxThoughness) {
		this.maxThoughness = maxThoughness;
	}
	
	@Override
	public String toString() {
		return String.format("P %d, T %d/%d", power, thoughness, maxThoughness);
	}
	
	
}
