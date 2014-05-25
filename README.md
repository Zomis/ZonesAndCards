ZonesAndCards
=============

This is an implementation and library for creating different kinds of card games. Both classic card games such as Hearts, and also some Trading Card Games are supported.

The basic idea is that each card game consists of the following:

- Zones: Player Hands, battlefields, discard piles (both shared and player-specific), etc.
- Cards: The actual cards that exists in different zones and that move between zones
- Card Models: What the actual cards represent, which kind of card it is
- Players: Can have player-specific zones, often have resources
- Phases: For example setting-up phases (such as exchanging cards before game is ready to play), player-specific phases to specify who's turn it is, phase in which only some actions may performed.

Note that although this code is meant to be very flexible and intended to be possible to create more or less any card game or trading card game you want, **there will be major API changes**.

#Directory structure and packages

###src

- net.zomis.cards: General game code containing the core classes
- net.zomis.cards.resources: ResourceMap system to keep track of resources mainly associated with players or cards

##classics

- net.zomis.cards.classics: Base code for all card games based on Hearts, Diamonds, Clubs and Spades. And wildcards.
- net.zomis.cards.hearts: [The traditional card game](http://en.wikipedia.org/wiki/Hearts) that exists in many versions of Microsoft Windows
- net.zomis.cards.idiot: Also known as [Idiot's Delight / Aces Up](http://meta.codereview.stackexchange.com/questions/1218/weekend-challenge-2)
- net.zomis.cards.poker: Not so much a game, contains code for checking poker hand evaluation. Originally written for [Code Review Weekend Challenge #2](http://meta.codereview.stackexchange.com/questions/1218/weekend-challenge-2)
- net.zomis.cards.turneight: A Swedish card game my grandmother taught me, resembles [Mau Mau](http://en.wikipedia.org/wiki/Mau_Mau_%28card_game%29) a lot.

##others

- net.zomis.cards.cwars2: Castle Wars implementation, a quite simple game based on three types of resources: Builders/Bricks, Recruits/Weapons, Wizards/Crystals. First to reach a castle of 100 or to destroy opponent's castle to 0 wins.
net.zomis.cards.greger: (Very incomplete) [UNO](http://en.wikipedia.org/wiki/Uno_%28card_game%29) is a Swedish first name, just like Greger. Greger sounds more fun though.


##The rest of the directories

- swing: Contains a simple UI
- test: Contains several tests. Not all working at the moment, as I'm doing a lot of Test-Driven Development. Failing tests is motivation for me about what to work on (Feel free to help with that!)
- wart: A TCG game I'd like to call "Wartstone".
- mdjq: Or possibly called "Zagic". Super-mega-big project, very little has been implemented.

#Dependencies

This project requires the following:

- https://github.com/Zomis/AIScorers
- https://github.com/Zomis/Commons
- https://github.com/Zomis/Fighting
- [Jackson Library](https://github.com/FasterXML/jackson)
- [Jackson Dataformat XML](https://github.com/FasterXML/jackson-dataformat-xml)
- stax2-2.1.jar (Jackson XML dependency)
