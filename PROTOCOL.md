## JSON Protocol

The server has a model on which the game runs. The client models are only used to show the game.

# Game Logic Messages in chronological order

- Player connects with server successfully: {"type":"connected","player names":[]}

- Player tries to log in: {"nick":"<nick>","type":"login"}

    - server accepts: { "type" : "login success" }
    - or server declines
        - lobby is full: { "type" : "login failed", "message" : "lobby is full" } // need to update if bug fixed
        - nickname already taken: {"type":"login failed","additional information":"Nickname already taken."}
    - if new player joins game: {"nick":"<nick>","type":"user joined"}

- Host client starts the game:
    - server accepts the start: {"type":"start game"}
        - server broadcasts to the users that the game started and content of the offerings (starting player marker as well as factory display)
        - and the names of the other players:
          {"type":"game started",
          "offerings":[["starting player marker"],
          ["<tile color>","<tile color>","<tile color>","<tile color>"],
          ["<tile color>","<tile color>","<tile color>","<tile color>"],
          ......
          ["<tile color>","<tile color>","<tile color>","<tile color>"],
          "player names":["<nick of player 1>", "<nick of player 2>, ..."]}

    - server declines the start because of not enough players: {"type":"game not startable","additional information":"not enough player"}

    - player chooses on a tile: {"index of offering":1,"type":"notify tile chosen","index of tile ":0}

      - server notifies which player will update the current offering. The chosen offering(factory display) is: ["<tile color>","<tile color>","<tile color>","<tile color>"]
      - and shows the chosen tile on that offering/factory display is <tile color>.

      - server broadcasts to the players that this move was possible and who ist the active player to chose the tile:
      {"name of active player":"<nick of active player>","type":"player has chosen tile"}


- player chooses a pattern line: 

   - The active player tries to place a tile <tile color> (index: <index of tile> on the <indes of the row> row of pattern lines.
   - and server broadcasts to the users that this move was possible and which row was taken: {"type":"place tile in pattern line","index of pattern line": <index of row in pattern line>}

                

- server broadcasts to the users whose turn is now: {"name of active player":"<nick of the active player>","pattern lines":
[["<tile color>"],
["<tile color>","<tile color>"],
["<tile color>","<tile color>","<tile color>"],
["<tile color>","<tile color>","<tile color>","<tile color>"],
["<tile color>","<tile color>","<tile color>","<tile color>","<tile color>"]],
"name of player who ended his turn":"<name of the player who ended this turn>","type":"next players turn",
"offerings":
[["starting player marker","<tile color>","<tile color>","<tile color>"],
[],
["<tile color>","<tile color>","<tile color>","<tile color>"],
......
["<tile color>","<tile color>","<tile color>","<tile color>"]],
"floor line":[]}

- server tells the non-active player that this move is not legal {"type":"not your turn"}


- player tries to place a tile directly into the floor line.

  - server broadcasts to the player that this move was possible and that the floor line was chosen.
             {"type" : "tile placed in floor line"}

  - server broadcasts to the player that the move was not legal (maybe if the player choses the floor line before he choses a tile)
  {"type" : "illegal move"} (//hier machen wir vllt nicht, kann später löschen)

- server tells the user that this move is not legal (//wir verwenden PopUps window now)

- server sends information to all players about who is the player with starting player marker and the status (name, pattern line, wall and points) of all players after a round ended:
  {"index of player with starting player marker":<index of the player>,
  "type":"round finished","player":
  [{"nick":"<nick of player 1>",
  "pattern lines":[
  ["tile color"],
  ["tile color","tile color"],
  ["tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"]],
  "wall":[
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"]],
  "floor line":[],
  "points":<points of the player>},
  [{"nick":"<nick of player 1>",
  "pattern lines":[
  ["tile color"],
  ["tile color","tile color"],
  ["tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"]],
  "wall":[
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"],
  ["tile color","tile color","tile color","tile color","tile color"]],
  "floor line":[],......]}


- Server broadcasts to all players with the name of the winner and the status (pattern lines, wall, floor line as well as points) of all players:

{"nick":"Hurray! Nils has won the game! You shall be allowed to help Queen MaXIne build her Cyber Palace with our beautiful cyber tiles!",
"type":"game finished",
"player":[
{"nick":"<nick of the player 1>",
"pattern lines":[
["tile color"],
["tile color","tile color"],
["tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"]],
"wall":[
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"]],
"floor line":[],"points": <points of player 1>},

{"nick":"Hurray! Nils has won the game! You shall be allowed to help Queen MaXIne build her Cyber Palace with our beautiful cyber tiles!",
"type":"game finished",
"player":[
{"nick":"<nick of the player 1>",
"pattern lines":[
["tile color"],
["tile color","tile color"],
["tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"]],
"wall":[
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"],
["tile color","tile color","tile color","tile color","tile color"]],
"floor line":[],"points": <points of player 1>}, ......]}