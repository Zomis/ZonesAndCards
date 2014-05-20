package net.zomis.cards.hstone.factory;

import static net.zomis.cards.hstone.factory.HSFilters.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.zomis.cards.hstone.FightModule;
import net.zomis.cards.hstone.HSFilter;
import net.zomis.cards.hstone.HStoneCard;
import net.zomis.cards.hstone.HStoneGame;
import net.zomis.cards.hstone.HStonePlayer;
import net.zomis.cards.hstone.HStoneRes;
import net.zomis.cards.hstone.ench.HStoneEnchForward;
import net.zomis.cards.hstone.ench.HStoneEnchSetTo;
import net.zomis.cards.hstone.ench.HStoneEnchSpecificPT;
import net.zomis.cards.hstone.ench.HStoneEnchantment;
import net.zomis.cards.hstone.events.HStoneMinionSummonedEvent;
import net.zomis.cards.hstone.events.HStoneTurnEndEvent;
import net.zomis.cards.hstone.events.HStoneTurnStartEvent;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;
import net.zomis.events.EventHandlerGWT;
import net.zomis.utils.ZomisList;

public class Battlecry {

	private static final HSFilter	ANY_TARGET	= combined(HSTargetType.MINION, HSTargetType.PLAYER);

	public interface HSGetCount {
		int determineCount(HStoneCard source, HStoneCard target);
	}
	
	public static HSGetCount fixed(final int count) {
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				return count;
			}
		};
	}
	
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
				FightModule.damage(source, target, damage);
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
				FightModule.heal(target, healing);
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
					FightModule.damage(source, target, damage);
				else target.freeze();
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
		if (attack < 0 || health < 0)
			throw new IllegalArgumentException("Attack and health must be >= 0");
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
//				source.getGame().addEnchantment(new HStoneEnchSpecificPT(source, attack, health));
				source.getResources().changeResources(HStoneRes.ATTACK, attack);
				source.getResources().changeResources(HStoneRes.HEALTH, health);
				source.getResources().changeResources(HStoneRes.MAX_HEALTH, health);
			}
		};
	}

	public static HStoneEffect removeDurabilityOppWeapon(final int durabilityToRemove) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCard weapon = source.getPlayer().getNextPlayer().getWeapon();
				if (weapon != null) {
					// TODO: A weapon should not count as a character!
					FightModule.damage(source, weapon, durabilityToRemove);
					weapon.getGame().cleanup();
				}
			}
		};
	}

	public static HStoneEffect silencer() {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				requireOnBattlefield(target);
				target.silence();
			}
		};
	}

	public static HStoneEffect summon(final String minion, final int count) {
		return new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, HStoneCard target) {
				final HStoneCardModel model = source.getGame().getCardModel(minion);
				if (model == null)
					throw new NullPointerException("Card Model not found: " + minion);
				StackAction summonAction = new StackAction() {
					@Override
					protected void onPerform() {
						for (int i = 0; i < count; i++) {
							CardZone<HStoneCard> zone = source.getPlayer().getBattlefield();
							if (zone.size() < HStonePlayer.MAX_BATTLEFIELD_SIZE) {
								HStoneCard card = zone.createCardOnBottom(model);
								source.getGame().callEvent(new HStoneMinionSummonedEvent(card));
							}
						}
					}
				};
				source.getGame().addStackAction(summonAction); // if this is not added to stack, cards get put on the battlefield in the wrong order
			}
		};
	}
	
	public static HStoneEffect summon(final String minion) {
		return summon(minion, 1);
	}

	public static HStoneEffect drawCard() {
		return drawCards(1);
	}

	public static HStoneEffect drawCards(final int cards) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStonePlayer player = source.getPlayer();
				for (int i = 0; i < cards; i++) {
					player.drawCard();
				}
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
		if (attack < 0 || health < 0)
			throw new IllegalArgumentException("Attack and health must be >= 0");
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				requireOnBattlefield(target);
//				source.getGame().addEnchantment(new HStoneEnchSpecificPT(target, attack, health));
				target.getResources().changeResources(HStoneRes.ATTACK, attack);
				target.getResources().changeResources(HStoneRes.HEALTH, health);
				target.getResources().changeResources(HStoneRes.MAX_HEALTH, health);
			}
		};
	}

	protected static void requireOnBattlefield(HStoneCard card) {
		if (card.getCurrentZone() != card.getPlayer().getBattlefield())
			throw new IllegalStateException("Card is not on the battlefield: " + card + " it is currently in " + card.getCurrentZone() + " with player " + card.getPlayer());
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
		return remove(HSAbility.DIVINE_SHIELD);
	}

	public static HStoneEffect remove(final HSAbility ability) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.removeAbility(ability);
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
				FightModule.damage(source, source.getPlayer().getNextPlayer().getPlayerCard(), damage);
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

	public static HStoneEffect equip(final String name) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO Auto-generated method stub
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

	public static HStoneEffect adjacents(final HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(final HStoneCard source, final HStoneCard target) {
				source.getGame().addStackAction(new StackAction() {
					@Override
					protected void onPerform() {
						requireOnBattlefield(source);
						HStoneCard left = source.getLeftAdjacent();
						HStoneCard right = source.getRightAdjacent();
						if (left != null)
							effect.performEffect(source, left);
						if (right != null)
							effect.performEffect(source, right);
					}
				});
			}
		};
	}

	public static HStoneEffect giveAbility(final HSAbility ability) {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				requireOnBattlefield(target);
				target.addAbility(ability);
			}
		};
	}
	
	public static HStoneEffect healMyHero(final int healing) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				heal(healing).performEffect(source.getPlayer().getPlayerCard(), target);
			}
		};
	}

	public static HStoneEffect toMinion(final HStoneEffect effect) {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				effect.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect to(final HSFilter filter, final HStoneEffect effect) {
		return new HStoneEffect(filter) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				effect.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect set(final HStoneRes resource, final int value) {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.getGame().addEnchantment(new HStoneEnchSetTo(target, HStoneRes.ATTACK, value));
			}
		};
	}

	public static HStoneEffect setAttack(final int attack) {
		return set(HStoneRes.ATTACK, attack);
	}

	public static HStoneEffect toAny(final HStoneEffect effect) {
		return new HStoneEffect(all()) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				effect.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect damageEnemyMinions(int damage) {
		return forEach(and(not(samePlayer()), allMinions()), null, damage(damage));
	}
	public static HStoneEffect destroyTarget() {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.destroy();
			}
		};
	}

	public static HStoneEffect freeze() {
		return new HStoneEffect() {
			
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.freeze();
			}
		}; 
	}

	public static HStoneEffect destroyManaCrystal() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_TOTAL, -1);
			}
		};
	}

	@Deprecated
	public static HStoneEffect selfPlayerDamage(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				FightModule.damage(source, source.getPlayer().getPlayerCard(), damage);
			}
		};
	}

	public static HStoneEffect damageMyHero(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				FightModule.damage(source, source.getPlayer().getPlayerCard(), damage);
			}
		};
	}

	public static HStoneEffect discardRandomCard() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCard random = ZomisList.getRandom(source.getPlayer().getHand().cardList(), source.getGame().getRandom());
				if (random != null)
					random.zoneMoveOnBottom(source.getPlayer().getDiscard());
			}
		};
	}
	
	public static HStoneEffect toRandom(final HSFilter filter, final HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> all = source.getGame().findAll(source, filter);
				HStoneCard random = ZomisList.getRandom(all, source.getGame().getRandom());
				effect.performEffect(source, random);
			}
		};
	}
	public static HStoneEffect evenChance(final HStoneEffect... effects) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				Random rand = source.getGame().getRandom();
				HStoneEffect effect = ZomisList.getRandom(effects, rand);
				effect.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect doNothing() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
			}
		};
	}

	public static HStoneEffect repeat(final HSGetCount times, final HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int count = times.determineCount(source, target);
				for (int i = 0; i < count; i++) {
					effect.performEffect(source, target);
				}
			}
		};
	}

	public static HStoneEffect destroyAllMinions() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final HStoneGame game = source.getGame();
				StackAction action = new StackAction() {
					@Override
					protected void onPerform() {
						for (HStonePlayer player : game.getPlayers()) {
							for (HStoneCard card : new ArrayList<HStoneCard>(player.getBattlefield().cardList())) {
								card.destroy();
							}
						}
					}
				};
				source.getGame().addStackAction(action);
			}
		};
	}

	public static HStoneEffect bothPlayers(final HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStonePlayer> players = source.getGame().getPlayers();
				for (HStonePlayer player : players) {
					effect.performEffect(player.getPlayerCard(), player.getPlayerCard());
				}
			}
		};
	}

	public static HStoneEffect giveCard(final String cardName) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getHand().createCardOnBottom(source.getGame().getCardModel(cardName));
			}
		};
	}

	public static HSGetCount oppWeaponDurability() {
		return new HSGetCount() {
			@Override
			public int determineCount(HStoneCard source, HStoneCard target) {
				HStoneCard weapon = source.getPlayer().getNextPlayer().getWeapon();
				if (weapon == null)
					return 0;
				return weapon.getHealth();
			}
		};
	}
	public static HStoneEffect copyMinionInOppDeckToMyBattlefield() {
//		 "Put a copy of a random minion from your opponent's deck into the battlefield"
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				LinkedList<HStoneCard> minions = ZomisList.filter2(source.getPlayer().getNextPlayer().getDeck().cardList(), new ZomisList.FilterInterface<HStoneCard>() {
					@Override
					public boolean shouldKeep(HStoneCard obj) {
						return obj.getModel().isMinion();
					}
				});
			 	HStoneCard minion = ZomisList.getRandom(minions, source.getGame().getRandom());
				minion.copyTo(source.getPlayer().getBattlefield());
			}
		};
	}

	public static HStoneEffect copyTwoCardsInOppDeckToMyHand() {
//		 "Copy 2 cards from your opponent's deck and put them into your hand"		
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> cards = new ArrayList<HStoneCard>(source.getPlayer().getNextPlayer().getDeck().cardList());
			 	CardZone<HStoneCard> destination = source.getPlayer().getHand();
			 	for (int i = 0; i < 2; i++) {
			 		if (destination.size() >= HStonePlayer.MAX_CARDS_IN_HAND)
			 			break;
				 	HStoneCard random = ZomisList.getRandom(cards, source.getGame().getRandom());
					random.copyTo(destination);
				 	cards.remove(random);
			 	}
			}
		};
	}

	public static HStoneEffect copyOppCardInHand() {
//		"Put a copy of a random card in your opponent's hand into your hand"
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> cards = new ArrayList<HStoneCard>(source.getPlayer().getNextPlayer().getHand().cardList());
			 	CardZone<HStoneCard> destination = source.getPlayer().getHand();
			 	for (int i = 0; i < 2; i++) {
			 		if (destination.size() >= HStonePlayer.MAX_CARDS_IN_HAND)
			 			break;
				 	HStoneCard random = ZomisList.getRandom(cards, source.getGame().getRandom());
					random.copyTo(destination);
				 	cards.remove(random);
			 	}
			}
		};
	}

	public static HStoneEffect stealMinionUntilEndOfTurn(HSFilter filter) {
//		 "Gain control of an enemy minion with 3 or less Attack until end of turn"
		return new HStoneEffect(and(not(samePlayer()), allMinions(), filter)) {
			@Override
			public void performEffect(HStoneCard source, final HStoneCard target) {
				final int turn = target.getGame().getTurnNumber();
				final HStonePlayer originalOwner = target.getPlayer();
				target.zoneMoveOnBottom(source.getPlayer().getBattlefield());
				target.getGame().registerHandler(HStoneTurnEndEvent.class, new EventHandlerGWT<HStoneTurnEndEvent>() {
					@Override
					public void executeEvent(HStoneTurnEndEvent event) {
						if (event.getGame().getTurnNumber() == turn) {
							target.zoneMoveOnBottom(originalOwner.getBattlefield());
						}
					}
				});
			}
		};
	}

	public static HStoneEffect shadowform() {
		//  "Your Hero Power becomes 'Deal 2 damage'. If already in Shadowform: 3 damage"
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int damage = 2;
				String name = "Shadowform";
				if (source.getPlayer().getHeroPower().getModel().getName().equals(name)) {
					damage = 3;
				}
				HStoneCardModel card = new HStoneCardFactory(name, 2, CardType.POWER).charge().effect(damage(damage)).card();
				source.getPlayer().setHeroPower(card);
			}
		};
	}

	public static HStoneEffect setOtherAttackEqualsHealth() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchForward(new HStoneEnchSetTo(target, HStoneRes.ATTACK, target.getHealth())));
			}
		};
	}

	public static HStoneEffect enchantSelfWithAttackEqualsHealth() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getGame().addEnchantment(new HStoneEnchForward(new HStoneEnchSpecificPT(source, 0, 0) {
					@Override
					public Integer getResource(HStoneCard card, HStoneRes resource, Integer resources) {
						if (resource == HStoneRes.ATTACK)
							return card.getHealth();
						return resources;
					}
				}));
			}
		};
	}

	public static HStoneEffect stealMinion(HSFilter filter) {
		return new HStoneEffect(HSFilters.and(HSTargetType.MINION, and(filter, not(HSFilters.samePlayer())))) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.zoneMoveOnBottom(target.getPlayer().getNextPlayer().getBattlefield());
				// TODO: Trigger summon minion event (or is that done automatically by the zone move?)
			}
		};
	}

	public static HStoneEffect doubleHealth() {
		return new HStoneEffect(HSTargetType.MINION) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int moreHealth = target.getHealth();
				source.getGame().addEnchantment(new HStoneEnchSpecificPT(target, 0, moreHealth));
				heal(moreHealth).performEffect(source, target);
			}
		};
	}

	public static HStoneEffect healAll(int heal, HSFilter filter) {
		return forEach(filter, null, heal(heal));
	}

	public static HStoneEffect destroyMinion(HSFilter filter) {
		return new HStoneEffect(filter) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.destroy();
			}
		};
	}
	
	public static HStoneEffect toMultipleRandom(HSGetCount getCount, HSFilter filter, HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int count = getCount.determineCount(source, target);
				List<HStoneCard> all = source.getGame().findAll(source, filter);
				for (int i = 0; i < count; i++) {
					HStoneCard random = ZomisList.getRandom(all, source.getGame().getRandom());
					all.remove(random);
					effect.performEffect(source, random);
				}
			}
		};
	}

	public static HStoneEffect transform(final String cardName) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				// TODO Auto-generated method stub
			}
		};
	}

	public static HStoneEffect selfDestruct() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.destroy();
			}
		};
	}

	public static HStoneEffect randomFriendlyMinion(final HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				LinkedList<HStoneCard> list = source.getPlayer().getBattlefield().cardList();
				list.remove(source);
				HStoneCard random = ZomisList.getRandom(list, source.getGame().getRandom());
				effect.performEffect(source, random);
			}
		};
	}

	public static HStoneEffect damageToEnemyHero(final int damage) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStonePlayer player = source.getPlayer();
				HStonePlayer opponent = player.getNextPlayer();
				FightModule.damage(source, opponent.getPlayerCard(), damage);
			}
		};
	}
	public static HStoneEffect untilMyNextTurn(HSFilter filter, HStoneEffect nowEffect, HStoneEffect laterEffect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final int currentTurn = source.getGame().getTurnNumber();
				List<HStoneCard> performOn = source.getGame().findAll(source, filter);
				for (HStoneCard card : performOn) {
					nowEffect.performEffect(source, card);
				}
				source.getGame().registerHandler(HStoneTurnStartEvent.class, new EventHandlerGWT<HStoneTurnStartEvent>() {
					@Override
					public void executeEvent(HStoneTurnStartEvent event) {
						if (event.getGame().getTurnNumber() == currentTurn + 2) {
							for (HStoneCard card : performOn) {
								laterEffect.performEffect(source, card);
							}
						}
					}
				});
			}
		};
	}
}
