package net.zomis.cards.systems;

import net.zomis.cards.cbased.CompPlayer;
import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.BattlefieldComponent;
import net.zomis.cards.components.HealthComponent;
import net.zomis.cards.components.TargetEffectComponent;
import net.zomis.cards.events2.DetermineActionEvent;
import net.zomis.cards.events2.FindUsableCardsEvent;
import net.zomis.cards.helpers.FightHelper;
import net.zomis.cards.iface.GameSystem;
import net.zomis.cards.iface.HasComponents;
import net.zomis.cards.iface.TargetOptions;
import net.zomis.cards.model.CardZone;

public class AttackWithBattlefieldSystem implements GameSystem {

	public AttackWithBattlefieldSystem() {
	}

	@Override
	public void onStart(FirstCompGame game) {
		game.registerHandler(FindUsableCardsEvent.class, this::addHandCards);
		game.registerHandler(DetermineActionEvent.class, this::determineAction);
	}

	public void addHandCards(FindUsableCardsEvent event) {
		BattlefieldComponent field = event.getPlayer().getRequiredComponent(BattlefieldComponent.class);
		event.addZoneToResult(field.getBattlefield());
	}
	
	private void determineAction(DetermineActionEvent event) {
		CompPlayer owner = event.getSource().getOwner();
		if (event.hasAction()) {
			return;
		}
		
		if (owner == null || !owner.hasComponent(BattlefieldComponent.class)) {
			return;
		}
		
		CardZone<?> zone = event.getSource().getCurrentZone();
		HasComponents currentPlayer = event.getSource().getGame().getCurrentPlayer();
		if (currentPlayer == null)
			return;
		
		if (zone == currentPlayer.getComponent(BattlefieldComponent.class).getBattlefield()) {
			event.setAction(new TargetEffectComponent(TargetOptions.singleTarget((src, dst) -> dst.hasComponent(HealthComponent.class)), (src, dst) -> FightHelper.fight(src, dst.getSingleTarget())));
		}
	}
}
