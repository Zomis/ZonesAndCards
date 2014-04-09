package net.zomis.cards.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.zomis.custommap.view.ZomisLog;
import net.zomis.replays.Replay;
import net.zomis.utils.ZomisUtils;

public class CardReplay extends Replay<CardGame<?, ?>, Card<?>> {

	public CardReplay(CardGame<?, ?> game) {
		super(game, ",");
	}

	@Override
	public String getMoveData(Card<?> move) {
		List<CardZone<?>> zones = move.getGame().getPublicZones();
		int zoneIndex = zones.indexOf(move.getCurrentZone());
		int cardIndex = move.getCurrentZone().cardList().indexOf(move);
		return zoneIndex + "-" + cardIndex;
	}

	@Override
	public Card<?> getMoveData(String data) {
		int zone = Integer.parseInt(ZomisUtils.textBefore(data, "-"));
		List<CardZone<?>> zones = this.game.getPublicZones();
		CardZone<?> cardZone = zones.get(zone);
		int cardPos = Integer.parseInt(ZomisUtils.textAfter(data, "-"));
		return cardZone.cardList().get(cardPos);
	}

	@Override
	public String[] extractMoves(String data) {
		return data.split(",");
	}

	@Override
	public String getInitialization() {
		StringBuilder str = new StringBuilder();
		Iterator<CardZone<?>> zoneIterator = game.getPublicZones().iterator();
		while (zoneIterator.hasNext()) {
			CardZone<?> zone = zoneIterator.next();
			Iterator<? extends Card<?>> it = zone.iterator();
			while (it.hasNext()) {
				Card<?> card = it.next();
				str.append(card.getModel().getName());
				if (it.hasNext())
					str.append(",");
			}
			if (zoneIterator.hasNext())
				str.append(";");
		}
		return str.toString();
	}

	@Override
	public void applyInitialization(String data) {
		Map<String, ? extends CardModel> cards = game.getCards();
		String[] zonesData = data.split(";", Integer.MAX_VALUE);
		System.out.println("Input data: " + data);
		for (String str : zonesData)
			System.out.println("Zone Data: " + str);
		
		if (game.getPublicZones().size() != zonesData.length)
			throw new AssertionError("Doomed for failure! " + game.getPublicZones().size() + " public zones and array contains data for " + zonesData.length);
		
		int zoneIndex = 0;
		for (CardZone<?> zone : game.getPublicZones()) {
			if (zoneIndex == zonesData.length)
				throw new IllegalStateException(zoneIndex + " of " + zonesData.length + " zones iterated");

			String zoneData = zonesData[zoneIndex];
			if (zonesData[zoneIndex].isEmpty()) {
				ZomisLog.warn("Empty zone: " + zone);
				zoneIndex++;
				continue;
			}
			
			String[] zoneCards = zoneData.split(",");
			for (String card : zoneCards) {
				CardModel cardModel = cards.get(card);
				if (cardModel == null)
					throw new NullPointerException("Card not found: " + card);
				zone.createCardOnBottom(cardModel);
			}
			ZomisLog.info("Added " + zoneCards.length + " cards to " + zone);
			
			zoneIndex++;
		}
		if (zoneIndex != zonesData.length)
			throw new IllegalStateException(zoneIndex + " of " + zonesData.length + " zones loaded");
	}

	@Override
	protected void performMove(Card<?> move) {
		game.click(move);
	}

}
