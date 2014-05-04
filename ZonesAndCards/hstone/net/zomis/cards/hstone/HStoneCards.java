package net.zomis.cards.hstone;

import net.zomis.cards.hstone.sets.FreeCards;
import net.zomis.cards.hstone.sets.ManaOneCards;
import net.zomis.cards.hstone.sets.ManaThreeCards;
import net.zomis.cards.hstone.sets.ManaTwoCards;
import net.zomis.cards.util.CardSet;

public class HStoneCards implements CardSet<HStoneGame> {

	@Override
	public void addCards(HStoneGame game) {
		// TODO: Page 45 and forward. Or rather: Make method HStoneLoad work.
		
		new FreeCards().addCards(game);
		new ManaOneCards().addCards(game);
		new ManaTwoCards().addCards(game);
		new ManaThreeCards().addCards(game);
	}
	
}
