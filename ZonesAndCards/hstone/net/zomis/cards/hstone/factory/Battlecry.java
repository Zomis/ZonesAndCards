package net.zomis.cards.hstone.factory;

import java.util.List;

import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.ench.HStoneEnchSpecificPT;
import net.zomis.cards.hstone.ench.HStoneEnchantment;
import net.zomis.cards.model.StackAction;

public class Battlecry {

	private static final HSFilter	ANY_TARGET	= combined(HSTargetType.MINION, HSTargetType.PLAYER);

	public static HSFilter combined(final HSTargetType... target) {
		return new HSFilter() {
			@Override
			public boolean shouldKeep(HStoneCard searcher, HStoneCard obj) {
				for (HSTargetType tar : target) {
					if (tar.shouldKeep(searcher, obj))
						return true;
				}
				return false;
			}
			
		};
	}
	
	public static HStoneEffect damage(final int damage, HSTargetType... targetType) {
		return new HStoneEffect(combined(targetType)) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.damage(damage);
			}
		};
	}

	public static HStoneEffect heal(final int healing) {
		return heal(healing, HSTargetType.MINION, HSTargetType.PLAYER);
	}
	
	public static HStoneEffect heal(final int healing, HSTargetType... target) {
		return new HStoneEffect(combined(target)) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.heal(healing);
			}
		};
	}

	public static HStoneEffect destroyOppWeapon() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getNextPlayer().removeWeapon();
			}
		};
	}

	public static HStoneEffect freezeOrDamage(final int damage) {
		return new HStoneEffect(ANY_TARGET) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				if (target.hasAbility(HSAbility.FROZEN))
					target.damage(damage);
				else target.addAbility(HSAbility.FROZEN);
			}
		};
	}

	public static HStoneEffect tempMana(final int additionalMana) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_AVAILABLE, additionalMana);
			}
		};
	}

	public static HStoneEffect damage(int damage) {
		return damage(damage, HSTargetType.MINION, HSTargetType.PLAYER);
	}

	public static HStoneEffect tempBoost(HSFilter targetFilter, final int attack, final int health) {
		return new HStoneEffect(targetFilter) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final HStoneGame game = source.getGame();
				final int turn = game.getTurnNumber();
				HStoneEnchantment enchantment = new HStoneEnchSpecificPT(target, attack, health) {
					@Override
					public boolean isActive() {
						return turn == game.getTurnNumber();
					}
				};
				source.getGame().addEnchantment(enchantment);
			}
		};
	}

	public static HStoneEffect selfPT(final int attack, final int health) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchSpecificPT(source, attack, health));
			}
		};
	}

	public static HStoneEffect removeDurabilityOppWeapon(final int durabilityToRemove) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCard weapon = source.getPlayer().getNextPlayer().getWeapon();
				if (weapon != null) {
					weapon.damage(durabilityToRemove);
					weapon.getGame().cleanup();
				}
			}
		};
	}

	public static HStoneEffect silencer() {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.silence();
			}
		};
	}

	public static HStoneEffect summon(final String minion) {
		return new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				final HStoneCardModel model = source.getGame().getCardModel(minion);
				StackAction summonAction = new StackAction() {
					@Override
					protected void onPerform() {
						source.getPlayer().getBattlefield().createCardOnBottom(model);
					}
				};
				source.getGame().addStackAction(summonAction); // if this is not added to stack, cards get put on the battlefield in the wrong order
			}
		};
	}

	public static HStoneEffect drawCard() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().drawCard();
			}
		};
	}


	public static HStoneEffect allDraw(final int cardCount) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				for (int i = 0; i < cardCount; i++) {
					source.getPlayer().drawCard();
					source.getPlayer().getNextPlayer().drawCard();
				}
			}
		};
	}

	public static HStoneEffect giveOpponentManaCrystal() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getNextPlayer().getResources().changeResources(HStoneRes.MANA_TOTAL, 1);
			}
		};
	}
	
	public static HStoneEffect otherPT(final int attack, final int health) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, final HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchSpecificPT(target, attack, health));
			}
		};
	}

	public static HStoneEffect forEach(final HSFilter filter, final HStoneEffect myEffect, final HStoneEffect otherEffect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> targets = source.getGame().findAll(source, filter);
				System.out.println("Targets is: " + targets);
				for (HStoneCard t : targets) {
					if (myEffect != null)
						myEffect.performEffect(source, t);
					if (otherEffect != null)
						otherEffect.performEffect(source, t);
				}
			}
		};
	}

	public static HStoneEffect removeShield() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.removeAbility(HSAbility.DIVINE_SHIELD);
			}
		};
	}

	public static HStoneEffect otherDestroy() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.destroy();
			}
		};
	}

	public static HStoneEffect damageToOppHero(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getNextPlayer().getPlayerCard().damage(damage);
			}
		};
	}

	public static HStoneEffect armor(final int armor) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.ARMOR, armor);
			}
		};
	}

	public static HStoneEffect equip(String string) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO Auto-generated method stub
			}
		};
	}

	public static HStoneEffect selfPlayerDamage(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getPlayerCard().damage(damage);
			}
		};
	}

	public static HStoneEffect combined(final HStoneEffect... effects) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				for (HStoneEffect eff : effects) {
					eff.performEffect(source, target);
				}
			}
		};
	}

}
