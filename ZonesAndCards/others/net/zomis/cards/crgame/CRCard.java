package net.zomis.cards.crgame;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

public class CRCard extends Card<CRCardModel> {

	private int age;
	private int	quality;
	private String	name;
	
	protected CRCard(CRCardModel model, CardZone<?> initialZone) {
		super(model);
		this.currentZone = initialZone;
		if (model.isZombie()) {
			quality = model.get(CRRes.QUALITY);
		}
	}

	public void newTurn() {
		age++;
		if (getModel().isUser()) {
			getPlayer().getResources().changeResources(CRRes.HOURS_AVAILABLE, getModel().getHoursAvailable());
		}
		else if (getModel().isZombie()) {
			getPlayer().getResources().changeResources(CRRes.QUALITY, quality);
		}
	}
	
	public CRPlayer getPlayer() {
		return (CRPlayer) super.getOwner();
	}

	public int getAge() {
		return age;
	}
	
	
	@Override
	public CRCardGame getGame() {
		return (CRCardGame) super.getGame();
	}

	public void changeQuality(int i) {
		this.quality += i;
	}

	public int getQuality() {
		return quality;
	}

	public void setName(String string) {
		this.name = string;
	}
	
	public String getName() {
		return name == null ? getModel().getName() : name;
	}
	
}
