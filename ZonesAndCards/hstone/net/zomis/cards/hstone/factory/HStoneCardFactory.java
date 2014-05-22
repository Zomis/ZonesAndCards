package net.zomis.cards.hstone.factory;

import net.zomis.cards.hstone.HSAction;
import net.zomis.cards.hstone.HSDoubleEventConsumer;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneClass;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.ench.HStoneEnchForward;
import net.zomis.cards.hstone.ench.HStoneEnchSpecificPT;
import net.zomis.cards.hstone.ench.HStoneEnchantment;
import net.zomis.cards.hstone.events.HStoneCardEvent;
import net.zomis.cards.hstone.events.HStoneCardPlayedEvent;
import net.zomis.cards.hstone.events.HStoneDoubleCardEvent;
import net.zomis.cards.hstone.events.HStoneMinionDiesEvent;
import net.zomis.cards.hstone.events.HStoneSecretRevealedEvent;
import net.zomis.cards.hstone.triggers.BattlecryTrigger;
import net.zomis.cards.hstone.triggers.CardEventDoubleTrigger;
import net.zomis.cards.hstone.triggers.CardEventTrigger;
import net.zomis.cards.hstone.triggers.DealDamageTrigger;
import net.zomis.cards.hstone.triggers.HStoneTrigger;



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

	public static HStoneCardFactory weapon(int manaCost, HStoneRarity rarity, int attack, int health, String name) {
		HStoneCardFactory factory = new HStoneCardFactory(name, manaCost, CardType.WEAPON);
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

	public HStoneCardFactory toTarget(HSFilter targets, HSAction effect) {
		card.setEffect(Battlecry.to(targets, effect));
		return this;
	}
	
	public HStoneCardFactory effect(HStoneEffect effect) { // TODO: Deprecate old style HStoneEffect and 'effect' and 'battlecry' methods
		// Spells, and Minions when they are at battlefield
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
	
	public HStoneCardFactory on(HStoneTrigger<?> trigger) {
		card.addTriggerEffect(trigger);
		return this;
	}

	public HStoneCardFactory windfury() {
		card.addAbility(HSAbility.WINDFURY);
		return this;
	}

	public HStoneCardFactory staticEffect(final HStoneEnchantment staticEnchantment) {
		return battlecry(new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchForward(staticEnchantment) {
					@Override
					public boolean isActive() {
						return source.isAlive();
					}
				});
			}
		});
	}

	public HStoneCardFactory spellDamage(int i) {
		this.card.spellDamage = i;
		return this;
	}

	public HStoneCardFactory deathrattle(HStoneEffect effect) {
		return on(HStoneMinionDiesEvent.class, effect, HSFilters.thisCard());
	}
	
	public HStoneCardFactory on(Class<? extends HStoneCardEvent> eventClass, HStoneEffect effect, HSFilter filter) {
		return on(new CardEventTrigger(eventClass, effect, filter));
	}

	public HStoneCardFactory overload(final int overload) {
		this.card.overload = overload;
		return on(HStoneCardPlayedEvent.class, new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_OVERLOAD, overload);
			}
		}, HSFilters.thisCard());
	}

	public HStoneCardFactory staticEffectOtherMurlocsBonus(final int attack, final int health) {
		return battlecry(new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchForward(new HStoneEnchSpecificPT(null, attack, health)) {
					@Override
					public boolean isActive() {
						return source.isAlive();
					}
					
					@Override
					public boolean appliesTo(HStoneCard card) {
						return card != source && card.isMinion() && card.getModel().isOfType(HStoneMinionType.MURLOC);
					}
				});
			}
		});
	}

	public HStoneCardFactory funText(String string) {
		return this;
	}

	public HStoneCardFactory staticEffectOtherFriendlyBeastsBonus(final int attack, final int health) {
		return battlecry(new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchForward(new HStoneEnchSpecificPT(null, attack, health)) {
					@Override
					public boolean isActive() {
						return source.isAlive();
					}
					
					@Override
					public boolean appliesTo(HStoneCard card) {
						return card != source && card.isMinion() && card.getPlayer() == source.getPlayer() && card.getModel().isOfType(HStoneMinionType.MURLOC);
					}
				});
			}
		});
	}

	public HStoneCardFactory forClass(HStoneClass clazz) {
		card.forClass(clazz);
		return this;
	}

	public HStoneCardFactory shroud() {
		// TODO: Implement shroud! (Can't be the target of spells or hero powers)
		return this;
	}

	public HStoneCardFactory noAttack() {
		card.addAbility(HSAbility.NO_ATTACK);
		return this;
	}

	public HStoneCardFactory staticPT(final HSFilter who, final int attack, final int health) {
		return battlecry(new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				source.addEnchantment(new HStoneEnchForward(new HStoneEnchSpecificPT(null, attack, health)) {
					@Override
					public boolean isActive() {
						return source.isAlive();
					}
					
					@Override
					public boolean appliesTo(HStoneCard card) {
						return who.shouldKeep(source, card); 
					}
				});
			}
		});
	}

	public HStoneCardFactory staticAbility(HSFilter who, HSAbility abilityGive) {
		return battlecry(new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				source.addEnchantment(new HStoneEnchantment() {
					@Override
					public boolean isActive() {
						return source.isAlive();
					}
					
					@Override
					public boolean appliesTo(HStoneCard card) {
						return who.shouldKeep(source, card); // card.isMinion() && 
					}
					
					@Override
					public boolean hasAbility(HStoneCard card, HSAbility ability, boolean hasAbility) {
						if (ability == abilityGive)
							return true;
						return hasAbility;
					}
				});
			}
		});
	}

	public HStoneCardFactory secret(Class<? extends HStoneDoubleCardEvent> clazz, HSDoubleEventConsumer effect, HSFilter triggerSource, HSFilter triggerTarget) {
		card.addTriggerEffect(new CardEventDoubleTrigger(clazz, combinedConsumer(revealTheSecret(), effect), 
				HSFilters.isActiveSecret().and(triggerSource), HSFilters.isActiveSecret().and(triggerTarget)));
		card.setEffect(new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.zoneMoveOnBottom(source.getPlayer().getSecrets());
			}
		});
		return this;
	}
	
	private HSDoubleEventConsumer combinedConsumer(HStoneEffect revealTheSecret, HSDoubleEventConsumer effect) {
		return new HSDoubleEventConsumer() {
			@Override
			public void handleEvent(HStoneCard listener, HStoneDoubleCardEvent event) {
				revealTheSecret.performEffect(listener, event.getSource());
				effect.handleEvent(listener, event);
			}
		};
	}

	public HStoneCardFactory secret(Class<? extends HStoneCardEvent> clazz, HStoneEffect effect, HSFilter trigger) {
		card.addTriggerEffect(new CardEventTrigger(clazz, Battlecry.combined(effect, revealTheSecret()), trigger.and(HSFilters.isActiveSecret())));
		card.setEffect(new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.zoneMoveOnBottom(source.getPlayer().getSecrets());
			}
		});
		return this;
	}

	private HStoneEffect revealTheSecret() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				
				source.getGame().callEvent(new HStoneSecretRevealedEvent(source));
				Battlecry.selfDestruct().performEffect(source, target);
			}
		};
	}

	public HStoneCardFactory poison() {
//		"Destroy any minion damaged by this minion"
		return on(new DealDamageTrigger(Battlecry.destroyTarget(), HSFilters.thisCard()));
	}
	
}
