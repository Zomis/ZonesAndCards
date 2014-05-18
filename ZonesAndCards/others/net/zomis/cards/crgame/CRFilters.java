package net.zomis.cards.crgame;


public class CRFilters {
	
	public CRFilter opponent() {
		return (src, target) -> src.getPlayer() != target.getPlayer();
	}
	
	public CRFilter isUser() {
		return (src, target) -> target.getModel().isUser();
	}
	
	public CRFilter ageLess(int lessThan) {
		return (src, target) -> target.getAge() <= lessThan; 
	}
	
	public CRFilter isZombie() {
		return (src, dst) -> dst.getModel().isZombie();
	}

	public CRFilter mine() {
		return (src, dst) -> dst.getPlayer() == src.getPlayer();
	}

	public CRFilter isBad() {
		return (src, dst) -> dst.getQuality() < 0;
	}

	public CRFilter qualityMore(int i) {
		return (src, dst) -> dst.getQuality() >= i;
	}

	
}
