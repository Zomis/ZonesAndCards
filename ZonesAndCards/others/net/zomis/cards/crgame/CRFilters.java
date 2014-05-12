package net.zomis.cards.crgame;

public class CRFilters {
	
	public static CRFilter opponent() {
		return (src, target) -> src.getPlayer() != target.getPlayer();
	}
	
	public static CRFilter isUser() {
		return (src, target) -> target.getModel().isUser();
	}
	
	public static CRFilter ageLess(int lessThan) {
		return (src, target) -> target.getAge() <= lessThan; 
	}
	
	public static CRFilter isZombie() {
		return (src, dst) -> dst.getModel().isZombie();
	}

	public static CRFilter mine() {
		return (src, dst) -> dst.getPlayer() == src.getPlayer();
	}

}
