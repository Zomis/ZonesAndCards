package test.net.zomis.cards;

import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.phases.GamePhase;

public class SimpleGame extends CardGame {
	public final CardZone a = new CardZone("Zone A");
	public final CardZone b = new CardZone("Zone B");
	public final CardZone c = new CardZone("Zone C");
	public final CardZone d = new CardZone("Zone D");
	public final CardZone global = new CardZone("Zone Global");
	
	public final String[] cards = new String[]{ "0 Bubu", "1 Bakkit", "2 Minken", "3 Zomis", "4 Tejpbit", "5 Kristian", "6 Mangakids", "7 Henrik", "8 Festis" };
	public final CardModel[] model = new CardModel[cards.length];

	public SimpleGame() {
		this.addZone(a);
		this.addZone(b);
		this.addZone(c);
		this.addZone(d);
		this.addZone(global);
		
		for (int i = 0; i < cards.length; i++) {
			model[i] = new CardModel(cards[i]);
			addCard(model[i]);
		}
		this.addPhase(new GamePhase());
	}

}
