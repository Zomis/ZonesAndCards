package net.zomis.cards.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.Player;
import net.zomis.cards.model.StackAction;
import net.zomis.custommap.CustomFacade;

public class CardView implements ActionListener {

	private final Card card;
	private final JButton button;
	
	private CardViewClickListener onClick;
	public static boolean	allKnown;
	public static CardViewTextStrategy text = new CardViewTextStrategy() {
		@Override
		public String textFor(Card card) {
			return card.toString();
		}
	};
	
	public CardView(Card card, CardViewClickListener listener) {
		super();
		this.card = card;
		this.button = new JButton();
		button.addActionListener(this);
		this.cardUpdate();
		CardViewClickListener empty = new CardViewClickListener() {
			@Override
			public void onCardClick(CardView cardView) {
				CustomFacade.getLog().w("Using empty click listener");
			}
		};
		this.onClick = (listener == null ? empty : listener);
	}

	public void cardUpdate() {
		
//		CustomFacade.getLog().i("Phase is " + phase + " and card is " + card + " in " + card.getCurrentZone());
		Player currentPlayer = card.getGame().getCurrentPlayer();
		boolean known = card.getCurrentZone().isKnown(currentPlayer) || allKnown;
		String html = known ? text.textFor(card) : "???";
		
		StackAction action = this.card.getGame().getAIHandler().click(getCard());
		button.setText("<html>" + html.replaceAll("\\n", "<br>") + "</html>");
		button.setEnabled(action == null ? false : action.isAllowed());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		CustomFacade.getLog().i("Clicked card " + this.card);
		this.onClick.onCardClick(this);
	}

	public Card getCard() {
		return card;
	}

	public Component getComponent() {
		return this.button;
	}

	public void setVisible(boolean b) {
		this.button.setVisible(b);
	}
	
}
