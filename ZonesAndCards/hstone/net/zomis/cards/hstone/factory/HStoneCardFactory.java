package net.zomis.cards.hstone.factory;


public class HStoneCardFactory {

	private HStoneCardModel card;

	public HStoneCardFactory(String name, int manaCost, CardType type) {
		this.card = new HStoneCardModel(name, manaCost, type);
	}
	
	public static HStoneCardFactory spell(int manaCost, HStoneRarity rarity, String name) {
		HStoneCardFactory factory = new HStoneCardFactory(name, manaCost, CardType.SPELL);
		factory.card.setRarity(rarity);
		return factory;
	}

	public static HStoneCardFactory minion(int manaCost, HStoneRarity rarity, int attack, int health, String name) {
		HStoneCardFactory factory = new HStoneCardFactory(name, manaCost, CardType.MINION);
		factory.card.setRarity(rarity);
		factory.card.setPT(attack, health);
		return factory;
	}

	public HStoneCardModel card() {
		return card;
	}

	public HStoneCardFactory battlecry(HStoneEffect effect) {
		// Minions
		card.addTriggerEffect(new BattlecryTrigger(effect));
		return this;
	}

	public HStoneCardFactory effect(HStoneEffect effect) {
		// Spells
		card.setEffect(effect);
		return this;
	}

	public HStoneCardFactory is(HStoneMinionType type) {
		card.addType(type);
		return this;
	}

	public HStoneCardFactory taunt() {
		card.addAbility(HSAbility.TAUNT);
		return this;
	}

	public HStoneCardFactory shield() {
		card.addAbility(HSAbility.DIVINE_SHIELD);
		return this;
	}

	public HStoneCardFactory charge() {
		card.addAbility(HSAbility.CHARGE);
		return this;
	}

	public HStoneCardFactory stealth() {
		card.addAbility(HSAbility.STEALTH);
		return this;
	}

	public HStoneCardFactory windfury() {
		card.addAbility(HSAbility.WINDFURY);
		return this;
	}

	public HStoneCardFactory defense() {
		card.addAbility(HSAbility.NO_ATTACK);
		return this;
	}

	public HStoneCardFactory enrage(Object selfPT) {
		return this;
	}

	public HStoneCardFactory staticEffect(Object typePTBonus) {
		// TODO Auto-generated method stub
		return this;
	}

	public HStoneCardFactory spellDamage(int i) {
		// TODO Auto-generated method stub
		return this;
	}

}
