package net.zomis.cards.systems;

import net.zomis.cards.cbased.FirstCompGame;
import net.zomis.cards.components.ZoneComponent;
import net.zomis.cards.iface.GameSystem;

public class AttackWithBattlefieldSystem implements GameSystem {

	private Class<? extends ZoneComponent> clazz;

	public AttackWithBattlefieldSystem(Class<? extends ZoneComponent> class1) {
		this.clazz = class1;
	}

	@Override
	public void onStart(FirstCompGame game) {

	}

}
