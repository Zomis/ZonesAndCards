package net.zomis.cards.crgame;

import net.zomis.cards.model.Card;
import net.zomis.cards.model.CardModel;
import net.zomis.cards.model.CardZone;
import net.zomis.cards.resources.ResourceMap;

public class CRCardModel extends CardModel {

	private final ResourceMap resMap;
	private final CRCardType type;
	private CREffect effect;
	private CRFilter targets;
	private String	description;
	
	public CRCardModel(String name, CRCardType type, int cost) {
		super(name);
		this.resMap = new ResourceMap();
		this.type = type;
		resMap.set(CRRes.HOURS_COST, cost);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected <E extends CardModel> Card<E> createCardInternal(CardZone<?> initialZone) {
		return (Card<E>) new CRCard(this, initialZone);
	}

	public int getHoursAvailable() {
		return resMap.getResources(CRRes.HOURS_AVAILABLE);
	}

	public CRCardType getType() {
		return type;
	}
	
	public boolean isUser() {
		return type == CRCardType.USER;
	}

	public boolean isZombie() {
		return type == CRCardType.ZOMBIE;
	}
	
	public boolean isSpell() {
		return type == CRCardType.SPELL;
	}

	public CRCardModel effect(CREffect effect) {
		this.effect = effect;
		return this;
	}
	
	public CREffect getEffect() {
		return effect;
	}

	CRCardModel quality(int i) {
		resMap.set(CRRes.QUALITY, i);
		return this;
	}
	
	public int getCost() {
		return resMap.getResources(CRRes.HOURS_COST);
	}

	public int get(CRRes resource) {
		return resMap.getResources(resource);
	}

	public CRCardModel giveHours(int i) {
		resMap.set(CRRes.HOURS_AVAILABLE, i);
		return this;
	}

	public CRCardModel toTarget(CREffect effect, CRFilter targets) {
		this.effect = effect;
		this.targets = targets;
		return this;
	}

	public boolean needsTargets() {
		return targets != null;
	}
	
	public CRFilter getTargets() {
		return targets;
	}

	public CRCardModel desc(String string) {
		this.description = string;
		return this;
	}
	
	public String getDescription() {
		return description;
	}
	
/*
Fight of the StackExchange sites
first to graduate wins, or destroy the opponent's site with zombies

x Two players
x Draw one card each turn
x x hours to spend each turn -- gains hours to spend from user cards, the more users - the more hours to spend.
x 6 cards in starting hand for each player
x Limit of 7 cards in hand, when have more: Simply do not draw any card
x Site Quality is health. When it's zero, site is dead. When it's 1000, site wins. (Castle Wars style)

- Zombies can be answered
? Good, unanswered zombies -- increases site quality *1
? Bad, unanswered zombies -- reduces site quality    *2
? Good, answered zombies -- increases site quality   *2
? Bad, answered zombies -- decreases site quality    *1

x Zombies affect site quality each turn
? Users affect zombies
- Users have hours to spend each turn. Not all users can spend hours on all things (permissions, and interests)

x Users and User Zone
x Zombies (questions) and Zombie Zone

RESOURCES(?)
------------
x Hours
x Users
x Questions
x Site Quality


CARDS
---------------
Data Explorer
Pimpin'
Hot Question!


x Exploding Bear Trap: Remove a new user
x Jamalize: 1h Zombie Quality +2
x Pimping Question: 1h Zombie Quality +1
x Close Zombie: 2h Remove Zombie
x tag:noooooooooooooo : Create a Linked-List Zombie with quality -3

x BTW.Work: Remove a random opponent user
x TTQW: Remove a random opponent user
x TTGTB: Remove a random opponent user
x CROLATDMS (Completely Ridiculous Overly Long Acronym That Doesn't Make Sense) : Create confusion and decrease opponent site quality with 50.
x Monking! : Add a user

w Napalm Strike: Increase all your Zombie Qualities with 1
We could really use your ammo: Add two new users
w Rep-maxed: User cannot do anything anymore. Remove the hours that user gives you.
w Thanks, Santa!: Increase a zombie with positive quality to 10 or 25, just for the badge.

A Car is not a Carpet: Change a Java Zombie to JavaScript

Card Names:
unicorns, waffles, bananas
# Ding! : Get someone's attention. Answer a Zombie.
- Malachi'd -- Starring anything and everything
- Serial Upvoting Reversal
- Don't Stack And Drive
- lol
- TS / RSA
- Head Shot
- COBOL
- Awakened Zombie
- [badge:nice-question]
- Off by one (1 vote missing for badge)
- No comment (Downvote)
- (removed): Kill a baby unicorn, or opponent discards a random card

SO:
6 to 8 weeks: 3h ????
Jon Skeet: 8h Answer all Zombies
Drop that and use jQuery: Transform ALL Zombies (also opponent) to jQuery Zombies
Freehand Circles: 3h Question quality -4 for not containing freehand circles
Unicorns: 3h Magic unicorn, add site quality
Waffles: 2h Take a break and eat waffles. Draw a card
Fastest Gun In The West: 0h Answer a Zombie with negative quality
Always Friday in Iceland: 3h Draw 3 cards
Close Police: Remove all Zombies
Burninate! : Remove all Zombies with a specific tag
- Hyphen-site
- Brainf*ck

Zombies
Answers
Reputation
tags
votes
moderators
edits
migrations
close
deletes
bounty
stars - chat
Zombie cards + zone
User cards + zone

*/
}
