package net.zomis.cards.classics;

import net.zomis.cards.model.Card;
import net.zomis.utils.ZomisList.FilterInterface;

public class ClassicCardFilter implements FilterInterface<Card> {
	private Suite	suite;
	private Integer	rank;
	private boolean	reversed;
	public ClassicCardFilter(Suite suite, Integer rank, boolean reversed) {
		this.suite = suite;
		this.rank = rank;
		this.reversed = reversed;
	}
	public ClassicCardFilter(Suite suite, Integer rank) {
		this(suite, rank, false);
	}
	public ClassicCardFilter(Suite suite) {
		this(suite, null);
	}
	public ClassicCardFilter(int rank) {
		this(null, rank);
	}
	@Override
	public boolean shouldKeep(Card obj) {
		ClassicCard model = (ClassicCard) obj.getModel();
		boolean keep = true;
		if (this.suite != null)
			keep = keep && this.suite.equals(model.getSuite());
		if (this.rank != null)
			keep = keep && this.rank == model.getRank();
		
		return keep ^ reversed; // keep XOR reversed
	}
}
