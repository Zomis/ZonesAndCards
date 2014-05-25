package net.zomis.cards.wart.factory;

import static net.zomis.cards.wart.factory.HSFilters.*;
import static net.zomis.cards.wart.factory.HSGetCounts.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.zomis.cards.model.CardZone;
import net.zomis.cards.model.StackAction;
import net.zomis.cards.wart.FightModule;
import net.zomis.cards.wart.HSAction;
import net.zomis.cards.wart.HSFilter;
import net.zomis.cards.wart.HSGetCount;
import net.zomis.cards.wart.HStoneCard;
import net.zomis.cards.wart.HStoneGame;
import net.zomis.cards.wart.HStonePlayer;
import net.zomis.cards.wart.HStoneRes;
import net.zomis.cards.wart.ench.HStoneEnchForward;
import net.zomis.cards.wart.ench.HStoneEnchSpecificPT;
import net.zomis.cards.wart.ench.HStoneEnchantment;
import net.zomis.cards.wart.events.HStoneMinionSummonedEvent;
import net.zomis.cards.wart.events.HStonePreAttackEvent;
import net.zomis.cards.wart.events.HStoneTurnEndEvent;
import net.zomis.cards.wart.events.HStoneTurnStartEvent;
import net.zomis.cards.wart.sets.HStoneOption;
import net.zomis.cards.wart.triggers.CardEventDoubleTrigger;
import net.zomis.cards.wart.triggers.CardEventTrigger;
import net.zomis.events.EventHandlerGWT;
import net.zomis.utils.ZomisList;

public class Battlecry {

	public static HStoneEffect damage(final int damage, HSFilter targetType) {
		return new HStoneEffect(targetType) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				FightModule.damage(source, target, damage);
			}
		};
	}

	public static HStoneEffect damage(final HSGetCount damage, HSFilter targetType) {
		return new HStoneEffect(targetType) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				FightModule.damage(source, target, damage.determineCount(source, target));
			}
		};
	}

	public static HStoneEffect heal(final int healing) {
		return heal(healing, all());
	}
	
	public static HStoneEffect heal(final int healing, HSFilter target) {
		return new HStoneEffect(target) {
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
				source.getPlayer().getNextPlayer().destroyWeapon();
			}
		};
	}

	public static HStoneEffect freezeOrDamage(final int damage) {
		return new HStoneEffect(all()) {
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
		return damage(damage, all());
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

	public static HStoneEffect selfPT(final HSGetCount attack, final HSGetCount health) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				selfPT(attack.determineCount(source, target), health.determineCount(source, target)).performEffect(source, target);
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
		return new HStoneEffect(allMinions()) {
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

	public static void requireOnBattlefield(HStoneCard card) {
		if (card == null)
			throw new NullPointerException("Card cannot be null");
		if (card.getCurrentZone() != card.getPlayer().getBattlefield())
			throw new IllegalStateException("Card is not on the battlefield: " + card + " it is currently in " + card.getCurrentZone() + " with player " + card.getPlayer());
	}

	public static HStoneEffect forEach(final HSFilter filter, final HSAction myEffect, final HSAction otherEffect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> targets = source.getGame().findAll(source, filter);
//				System.out.println("Targets is: " + targets);
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
				HStoneCard card = source.getPlayer().getSpecialZone().createCardOnBottom(source.getGame().getCardModel(name));
				source.getPlayer().equip(card);
			}
		};
	}

	public static HStoneEffect combined(final HSAction... effects) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				for (HSAction eff : effects) {
					eff.performEffect(source, target);
				}
			}
		};
	}

	public static HStoneEffect adjacents(final HSAction effect) {
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
		return new HStoneEffect(allMinions()) {
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
				heal(healing).performEffect(source, source.getPlayer().getPlayerCard());
			}
		};
	}

	public static HStoneEffect toMinion(final HStoneEffect effect) {
		return to(allMinions(), effect);
	}

	public static HStoneEffect to(final HSFilter filter, final HSAction effect) {
		return new HStoneEffect(filter) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				effect.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect set(final HStoneRes resource, final int value) {
		return new HStoneEffect(allMinions()) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.getResources().set(resource, value);
			}
		};
	}

	public static HStoneEffect setAttack(final int attack) {
		return set(HStoneRes.ATTACK, attack);
	}

	public static HStoneEffect toAny(final HStoneEffect effect) {
		return to(all(), effect);
	}

	public static HStoneEffect damageEnemyMinions(int damage) {
		return forEach(allMinions().and(opponent()), null, damage(damage));
	}
	
	public static HStoneEffect destroyTarget() {
		return new HStoneEffect(allMinions()) {
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
	
	public static HStoneEffect toRandom(final HSFilter filter, final HSAction effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				List<HStoneCard> all = source.getGame().findAll(source, filter);
				HStoneCard random = ZomisList.getRandom(all, source.getGame().getRandom());
				if (random != null)
					effect.performEffect(source, random);
			}
		};
	}
	public static HStoneEffect evenChance(final HSAction... effects) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				Random rand = source.getGame().getRandom();
				HSAction effect = ZomisList.getRandom(effects, rand);
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

	public static HStoneEffect repeat(final HSGetCount times, final HSAction effect) {
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

	public static HStoneEffect bothPlayers(final HSAction effect) {
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
				HStoneCardModel model = source.getGame().getCardModel(cardName);
				if (model == null)
					throw new NullPointerException("CardModel not found for " + cardName);
				source.getPlayer().getHand().createCardOnBottom(model);
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
			 	if (minion != null)
			 		minion.copyTo(source.getPlayer().getBattlefield());
			 	else summon("Shadow of Nothing").performEffect(source, target);
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
				 	if (random == null)
				 		return;
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
				 	if (random == null)
				 		return;
					random.copyTo(destination);
				 	cards.remove(random);
			 	}
			}
		};
	}

	public static HStoneEffect stealMinionUntilEndOfTurn(HSFilter filter) {
//		 "Gain control of an enemy minion with 3 or less Attack until end of turn"
		return new HStoneEffect(opponentMinions().and(filter)) {
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
//				source.getGame().addEnchantment(new HStoneEnchForward(new HStoneEnchSetTo(target, HStoneRes.ATTACK, target.getHealth())));
				target.getResources().set(HStoneRes.ATTACK, target.getHealth());
			}
		};
	}

	public static HStoneEffect enchantSelfWithAttackEqualsHealth() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.addEnchantment(new HStoneEnchForward(new HStoneEnchSpecificPT(source, 0, 0) {
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

	public static HStoneEffect stealMinion(final HSFilter filter) {
		return new HStoneEffect(allMinions().and(opponent()).and(filter)) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.zoneMoveOnBottom(target.getPlayer().getNextPlayer().getBattlefield());
				if (!target.hasAbility(HSAbility.CHARGE)) {
					target.getResources().set(HStoneRes.ACTION_POINTS_USED, 255);
				}
				// TODO: Trigger summon minion event (or is that done automatically by the zone move?)
			}
		};
	}

	public static HStoneEffect doubleHealth() {
		return new HStoneEffect(allMinions()) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				int moreHealth = target.getHealth();
				otherPT(0, moreHealth).performEffect(source, target);
//				source.getGame().addEnchantment(new HStoneEnchSpecificPT(target, 0, moreHealth));
//				heal(moreHealth).performEffect(source, target);
			}
		};
	}

	public static HStoneEffect healAll(int heal, HSFilter filter) {
		return forEach(filter, null, heal(heal));
	}

	public static HStoneEffect destroyMinion(HSFilter filter) {
		return new HStoneEffect(filter.and(allMinions())) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.destroy();
			}
		};
	}
	
	public static HStoneEffect toMultipleRandom(HSGetCount getCount, HSFilter filter, HSAction effect) {
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
		return new HStoneEffect(allMinions()) {
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

	public static HStoneEffect randomFriendlyMinion(final HSAction effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				LinkedList<HStoneCard> list = source.getPlayer().getBattlefield().cardList();
				list.remove(source);
				HStoneCard random = ZomisList.getRandom(list, source.getGame().getRandom());
				if (random != null)
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
	
	public static HStoneEffect untilMyNextTurn(HSFilter filter, HSAction nowEffect, HSAction laterEffect) {
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
	
	public static HStoneEffect oppSummon(final String cardName, final int count) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				summon(cardName, count).performEffect(source.getPlayer().getNextPlayer().getPlayerCard(), target);
			}
		};
	}

	public static HStoneEffect destroyAtEndOfTurn() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final int turn = source.getGame().getTurnNumber();
				source.addTrigger(new CardEventTrigger(HStoneTurnEndEvent.class, destroy(source), new HSFilter() {
					@Override
					public boolean shouldKeep(HStoneCard searcher, HStoneCard target) {
						return searcher.getGame().getTurnNumber() == turn;
					}
				}));
			}
		};
	}

	public static HStoneEffect destroy(final HStoneCard card) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				card.destroy();
			}
		};
	}
	public static HStoneEffect tempBoostToMyHero(int attack, int health) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				tempBoost(null, attack, health).performEffect(source, source.getPlayer().getPlayerCard());
			}
		};
	}

	public static HStoneEffect oppDraw(final int i) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				final HStoneCard opp = source.getPlayer().getNextPlayer().getPlayerCard();
				drawCards(i).performEffect(opp, opp);
			}
		};
	}

	public static HStoneEffect manaPermanentFilled(final int mana) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_TOTAL, mana);
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_AVAILABLE, mana);
			}
		};
	}
	
	public static HStoneEffect manaPermanentEmpty(final int mana) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().getResources().changeResources(HStoneRes.MANA_TOTAL, mana);
			}
		};
	}

	public static HStoneEffect toTargetAndAdjacents(HSFilter filter, HStoneEffect toTarget, HStoneEffect toAdjacents) {
//		  "Deal 5 damage to a minion and 2 damage to adjacent ones"
		return new HStoneEffect(allMinions().and(filter)) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				toTarget.performEffect(source, target);
				adjacents(toAdjacents).performEffect(source, target);
			}
		};
	}

	public static HStoneEffect ifElse(HSFilter condition, HStoneEffect ifTrue, HStoneEffect ifFalse) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				if (condition.shouldKeep(source, target)) {
					ifTrue.performEffect(source, target);
					return;
				}
				ifFalse.performEffect(source, target);
			}
		};
	}

	public static HStoneEffect destroyEnemySecrets() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStonePlayer player = source.getPlayer().getNextPlayer();
				player.getSecrets().moveToBottomOf(player.getDiscard());
			}
		};
	}

	public static HStoneEffect toFriendlyBeast(final HStoneEffect combined) {
		return to(allMinions().and(minionIs(HStoneMinionType.BEAST)).and(samePlayer()), combined);
	}
	
	public static HStoneEffect weaponBonus(int attack, int durability) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				selfPT(attack, durability).performEffect(source.getPlayer().getWeapon(), null);
			}
		};
	}

	public static HStoneEffect iff(HSFilter condition, HSAction action) {
		return new HStoneConditionalEffect(condition, action);
	}

	public static HStoneEffect unsummon() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.unsummon();
			}
		};
	}

	public static HStoneEffect chooseOne(HStoneOption... options) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				for (HStoneOption option : options) {
					source.getGame().getTemporaryZone().createCardOnBottom(option);
				}
				source.getGame().setTemporaryFor(source);
			}
		};
	}

	public static HStoneEffect fourDamageToAnEnemyAnd1DamageToOtherEnemies() {
		final HStoneEffect damage4 = damage(4);
		final HStoneEffect damage1 = damage(1);
		return new HStoneEffect(opponent()) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				damage4.performEffect(source, target);
				
				List<HStoneCard> others = target.getGame().findAll(target, samePlayer().and(anotherCard()));
				for (HStoneCard card : others) {
					damage1.performEffect(source, card);
				}
			}
		};
	}
	
	public static HStoneEffect toSelf(HStoneEffect effect) {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				effect.performEffect(source, source);
			}
		};
	}
	
	public static HStoneEffect comboOrNothing(HStoneEffect effect) {
		return iff(isCombo, effect);
	}

	public static HStoneEffect destroyMyWeapon() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				source.getPlayer().destroyWeapon();
			}
		};
	}

	public static HStoneEffect drawCardAndDealDamageEqualToCost() {
		return new HStoneEffect() {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				HStoneCard card = source.getPlayer().drawCard();
				int cost = card.getManaCost();
				damage(cost).performEffect(source, target);
			}
		};
	}

	public static HStoneEffect addEnchantOnAttackDrawCard() {
//		 TODO: "Choose a minion.  Whenever it attacks, draw a card"
		return new HStoneEffect(allMinions()) {
			@Override
			public void performEffect(HStoneCard source, HStoneCard target) {
				target.addTrigger(new CardEventDoubleTrigger(HStonePreAttackEvent.class, (listener, event) -> source.getPlayer().drawCard(), samePlayer(), null));
			}
		};
	}

	public static final HSAction addEnchantOnMyTurnStartDestroy = 
		(src, dst) -> dst.addTrigger(new CardEventTrigger(HStoneTurnStartEvent.class, selfDestruct(), (src2, dst2) -> src2.getPlayer() == src.getPlayer()));

	public static HStoneEffect toAdjacentsAndTarget(HStoneEffect effect) {
		return toTargetAndAdjacents(all(), effect, effect);
	}

}
