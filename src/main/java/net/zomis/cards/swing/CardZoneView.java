package net.zomis.cards.swing;

import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.zomis.cards.events.card.ZoneChangeEvent;
import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardZone;

public class CardZoneView implements CardViewClickListener {
	
	private final JPanel thisPanel = new JPanel();
	private final CardZone<?> zone;
	private final JPanel cards;
	private final List<CardView> cardViews;
	private int	viewLimit;

	private CardViewClickListener listener = new CardViewClickListener() {
		@Override
		public void onCardClick(CardView cardView) {} // Do nothing here on purpose -- Null Object pattern
	};

	public CardZoneView(CardZone<?> zone) {
		super();
		this.cardViews = new LinkedList<CardView>();
		this.zone = zone;
		thisPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));
		thisPanel.add(new JLabel(zone.getFullName()));

		cards = new JPanel();
		cards.setLayout(new FlowLayout());
		thisPanel.add(cards);

		for (Card<?> card : zone) {
			addCard(card);
		}
	}
	
	public void removeCard(Card<?> card) {
		Iterator<CardView> it = cardViews.iterator();
		
		while (it.hasNext()) {
			CardView next = it.next();
			if (next.getCard() == card) {
				cards.remove(next.getComponent());
				it.remove();
			}
		}
	}
	
	public CardZone<?> getZone() {
		return zone;
	}
	
	public JPanel getThisPanel() {
		return thisPanel;
	}
	
	public void addCard(Card<?> card) {
		CardView cv = new CardView(card, this);
		cardViews.add(cv);
		cards.add(cv.getComponent());
	}
	
	public void setListener(CardViewClickListener listener) {
		this.listener = listener;
	}
	
	public void updateTexts() {
		int index = 0;
		for (CardView cv : cardViews) {
			cv.cardUpdate();
			cv.setVisible(index >= cardViews.size() - viewLimit);
			index++;
		}
	}
	
	public void onZoneChangeEvent(ZoneChangeEvent event) {
		this.removeCard(event.getCard());
		if (this.getZone() == event.getToCardZone()) {
			this.addCard(event.getCard());
		}
	}

	@Override
	public void onCardClick(CardView cardView) {
		this.listener.onCardClick(cardView);
	}

	public void setViewLimit(int i) {
		this.viewLimit = i;
		this.updateTexts();
	}

	public void recreateFromScratch() {
		cards.removeAll();
		for (Card<?> card : this.zone)
			this.addCard(card);
	}

}
