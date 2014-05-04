package net.zomis.cards.greger;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardGame;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.phases.PlayerPhase;

/**
 * @author Zomis
 *
 */
public class GregerGame extends CardGame<Player, GregerCardModel> {
	// TODO: GregerGame: http://service.mattel.com/instruction_sheets/42885-0920_Uno_30th_Instr.pdf
	private List<GregerSuite> suites = new LinkedList<GregerSuite>();
	private CardZone<Card<GregerCardModel>> deck;
	private CardZone<Card<GregerCardModel>> discard;
	private Map<Player, CardZone<Card<GregerCardModel>>> playerHands = new TreeMap<Player, CardZone<Card<GregerCardModel>>>();
	
	public GregerGame() {
		suites.add(new GregerSuite("RED"));
		suites.add(new GregerSuite("GREEN"));
		suites.add(new GregerSuite("BLUE"));
		suites.add(new GregerSuite("YELLOW"));

		this.deck = new CardZone<Card<GregerCardModel>>("Deck");
		
		for (GregerSuite gs : this.suites) {
			for (int i = 0; i <= 9; i++) {
				GregerCardModel gcm = new GregerCardModel(gs, i);
				this.addCard(gcm);
				deck.createCardOnTop(gcm);
				if (i > 0)
					deck.createCardOnTop(gcm); // Cards > 0 has two copies.
			}
			GregerCardModel gcm;
			
			GregerEffect[] ges = new GregerEffect[]{ GregerEffect.STOP, GregerEffect.PICK2, GregerEffect.REVERSE };
			for (GregerEffect ge : ges) {
				gcm = new GregerCardModel(gs, ge);
				this.addCard(gcm);
				for (int i = 0; i < 2; i++)	
					deck.createCardOnTop(gcm);
			}
		}
		GregerCardModel gcm = new GregerCardModel(null, GregerEffect.COLOR);
		this.addCard(gcm);
		for (int i = 0; i < 4; i++)
			deck.createCardOnTop(gcm);
		
		gcm = new GregerCardModel(null, GregerEffect.COLOR_TAKE4);
		this.addCard(gcm);
		for (int i = 0; i < 4; i++)
			deck.createCardOnTop(gcm);
		
		this.discard = new CardZone<Card<GregerCardModel>>("Discard");
		this.discard.setGloballyKnown(true);
		
		this.deck.shuffle();
	}
	
	public void addPlayer(String name) {
		Player pl = new Player().setName(name);
		this.addPlayer(pl);
		CardZone<Card<GregerCardModel>> hand = new CardZone<Card<GregerCardModel>>("Hand", pl);
		hand.setKnown(pl, true);
		this.playerHands.put(pl, hand);
		
		this.addPhase(new PlayerPhase(pl));
	}
	
	
}
