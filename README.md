# Cyberzul Game

_Cyberzul -Mode: On!_

We are in the early days of cyber civilization. The legendary Queen MaXIne has just visited our beloved Zulias for the
very first time. Awestruck by the interior beauty of the rectangular slabs of baked bits and bytes (for simplicity we
will refer to them as „tiles“) used in our home city, she decided to make it the center of her queendom. A seemingly
endless tiling phase has just begun! _Cyberzul_ is a game that not only allows all players to gain the skills to help
her establish her empire and lead our civilization into a golden age. No! Beyond that, the winner will be honored by
being allowed to help Queen MaXIne build her Cyber Palace with our beautiful cyber tiles!

_Cyberzul-Mode: Off!_

## Developed for the SEP summer term 2022

It is a game from our project for LMU in the summer term 2022. With it the player can play the _Cyberzul_ Game on
his/her personal computer. For features that are going to come in the future, we refer to our CHANGELOG. Currently, one
is able to play the game in hot seat, network, and single player mode.

## Dependencies

We use Java 17 and JUnit 5.8.1 for this project. It was tested on Windows 11 with Java 17.0.3, so no other guarantees
can be made.

## How to start the application

Everything is already compiled. Make sure you have installed Java version 17 on your computer. Execute the jar-file with
command:

`java -jar team12.jar`

## How to play the game

_Cyberzul-Mode: On again!_

I see you have come here because you want to be more, not just another byty traveller, but a mighty tiler - a tiler of
cyber tiles … lucky you!, you have come to the right place.

In this section we will explain to you how to play _Cyberzul_. We’ll start with the basics: how to lay out tiles
according to the centuries-old tradition of the game. But we will come all the way to how to win a game of _Cyberzul_ -
that is to be honored with the permission to lay out your tiles in Queen MaXIne's cyber palace.

### Which modes are there to pick

First, you will have to pick a mode: Solo, hot seat or network mode. What do all of them have in common? You will have
to type at least one nickname for the game and you will play with either two, three, or four players afterwards.
Moreover, you are able to choose, if you want to enable _Bullet-Mode_ ... that is every player will have 12 seconds and
12 seconds only to make his/her move, otherwise an AI will make the move for him/her. (We suggest Cyberzul-Newbys to 
their first games with Bullet-Mode turned off.)

- **Solo**: Here you will play alone - against one, two, or three AI-players.
- **Hot seat**: Here you play on one device - against the players you add before. Type the players into the input field
  and add them. And as soon as you have enough players, you can start the game.
- **Network**: Here you are able to play with your friends via Wi-Fi. The first player must create a server, others are
  then able to join him/her by typing in the IP-address used.

### What's what in the game - a screenshot

*TODO - when final.*

### How and where to pick a tile from

You can choose a tile either from the _Cyber Mines_ or the _Stack_ - both are located above your player board. By
clicking a tile of any color, you will receive ALL of the tiles of that color on that Cyber Mine or in the _Stack_. If
you are the one to make the first move of a round, you should click on a tile from the _Cyber Mines_ as you can just see
the Starting Player Marker on the _Stack_ in the beginning of each round.

Next, we will explain to you how tiles get into the _Stack_. So then you will be able to choose tiles from the _Stack_,
too. It works the same. However, if you are the first player of a given round to take a tile from the _Stack_, the
Starting Player Marker will be placed in your floor line automatically. On the one hand, this means minus points; on the
other hand, you will be the player to start the next round. Choose wisely!

Oh, and by the way: This is _Cyberzul_. It is fun, it is fancy, and it is fast. You have 12 seconds for your move. … So,
choose wisely … and do not take too much time for it, otherwise, the AI will make the move for you. (But no worries, 
only you have Bullet-mode turned on; otherwise, you have all the time in the world.)

### Where to place a tile

The tile(s) you have picked should be placed on the pattern lines. Pick one by clicking on it. However, there are some
rules.

- You shall not place tiles in a pattern line, that already holds tiles of a different color. And once all spaces of a
  pattern line are filled, that line is considered complete. If you have picked up more tiles than you can place in your
  chosen pattern line, you must place the excess tiles in the floor line. (Huh? Floor line? Wait a minute, we will get
  to that.)
- You shall not place tiles in a pattern line, that already has a tile of the chosen color activated on the wall. (
  Huh_2? Wall? Yes, we will get to that. For the first round: Don’t worry about the second rule.)
- Sometimes horrible things happen. Sometimes, as the game goes on, you will not be able to place any tiles in any of
  the pattern lines. Then you will have to put them onto the floor line directly. You might have guessed: It is better
  to avoid that.

As soon as you place your tile(s), all other tiles of that Cyber Mine will be placed in the _Stack_.

But … don’t worry. … Let’s be honest, we all have been beginner tilers. When we just started out playing _Cyberzul_ (
back then it had a different name), we basically did everything wrong one could do, so we decided to create _Cyberzul_
in a way that you would (and in fact could only) play it correctly. The game will tell you, when you might have clicked
on a pattern line, where you cannot place your chosen tiles.

### When does a round finish

A round finishes when there are no tiles anymore: neither on the _Cyber Mines_, nor in the _Stack_. If the game hasn’t
finished yet, the next round will begin afterwards with fresh tiles on the _Cyber Mines_.

### And what happens then

Tiling! We start tilin’! At the end of a round, all full pattern lines activate a cyber tile at your wall! And that’s
not just beautiful, you will receive points for that, too.

### When does the game end

The game ends after the round in which at least one player has completed a horizontal line of five consecutive tiles on
his/her wall.

### Who is the winner

The winner is the player with the most points at the end of the game. "How do I get points?“, you ask. And here is the
answer:

### How do I get points during the game

We have already mentioned, a full pattern line will lead to the activation of a cyber tile on your wall. Here is how you
then get points:

- If there are no tiles directly adjacent (vertically or horizontally) to the newly placed tile, you get one point.
- If there are any tiles adjacent, however, the following happens:
    - The game checks if there are one or more tiles horizontally linked to the newly placed cyber tile. If so, it
      counts all these linked tiles (including the newly placed one) and rewards you with that many points.
    - Then it checks if there are one or more cyber tiles vertically linked to the newly placed tile. If so, it counts
      all these linked tiles (including the newly placed tile) and rewards you with that many points.

Finally, at the end of the wall-tiling phase, it checks if you have any tiles in the floor line. For the first two
tiles, you lose one point each, for the next three tiles, you lose two points each, and for the last two tiles, you loos
three points each.

### How do I get points at the end of the game

The game ends right after the wall-tiling phase in wich at least one player has completed at leas one horizontal line of
five consecutive tiles on his/her wall.

Once the game has ended, you will be rewarded with additional points if you have achieved the following goals:

- Two points for each complete horizontal line of five consecutive tiles on your wall
- Seven points for each complete vertical line of five consecutive tiles on your wall
- Ten points for each color of which you have placed all five tiles on your wall

The player with the most points on his/her score track wins the game. In the case of a tie, the tied player with more
complete horizontal lines wins the game. If that does not break the tie, the win will be shared.

### Greetings

The most important rule is to have fun playing the game!

May Tilora, the goddess of home building and cyber tiling, be with you,

Your Cybermasters - also known as Gang of Five

P.S.: _Cyberzul_ is based on an even older, an ancient game, you might say, called Azul. Here are the instructions for
that game:

_Cyberzul-Mode: Off!_

You can find game instructions for the Azul game
on [this website](https://tesera.ru/images/items/1108676/EN-Azul-Rules.pdf).

## Image sources

TODO