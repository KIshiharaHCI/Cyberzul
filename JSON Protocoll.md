## JSON Protocol

The server has a model on which the game runs. The client models are only used to show the game.

# Game Logic Messages in chronologic order

- user tries to log in { "type" : "login", "nick" : "<nick>" }
    - server accepts { "type" : "login success" }
    - or server declines 
        - lobby is full { "type" : "login failed", "message" : "lobby is full" }
        - name already taken { "type" : "login failed", "message" : "nick already taken" }

- host client starts the game { "type" : "start game" }
    - server accepts the start
        - server broadcasts to the users that the game started and the content of the factory displays 
        { "type" : "game started", 
         "factory displays" : 
            [
                [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ], 
                [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ], 
                [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ],
                 [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ]
                 ... 
            ]
        }
        - server broadcasts to the users the names of the other players
        {"type" : "names of the other players", "names" : [
            "<name of player 1>",
            "<name of player 2>",
            "<name of player 3>",
            ...
        ]}
        - server broadcasts to the users the name of the player who start
        {"type" : "active player", "name" : "<name of the starting player>"}

    - server declines the start because of not enough players { "type" : "login failed", "message" : "not enough players" }

- player chooses on a tile {"type" : "chose tile", "offering" : "<name of the offering>", "tile color" : "<tile color>" }

    - server broadcasts to the users that this move was possible and which tile on which factory display was taken.
    {"type" : "tile chosen", "offering" : "<name of the offering>", "tile color" : "<tile color>" }

        - player chooses a pattern line
        {"type" : "chose pattern line" : "pattern line index" : "<index of pattern line>" }

                - server broadcasts to the users that this move was possible and which row was taken.
                {"type" : "tile placed", "pattern line index" : "<index of pattern line>" }

                    - server broadcasts to the users whose turn is now. {"type" : "active player", "name" : "<name of the starting player>"}

                - server tells the user that this move is not legal {"type" : "illegal move"}

        - player chooses floor line {"type" : "chose floor line" }

                - server broadcasts to the users that this move was possible and that the floor line was chosen.
                {"type" : "tile placed in floor line"}

                - server broadcasts to the users that the move was not legal (maybe if the player choses the floor line before he choses a tile)
                {"type" : "illegal move"}

    - server tells the user that this move is not legal
    {"type" : "illegal move", "message" : "tile can't be chosen" }

- server sends the new content of the factory displays after a round ended
        {"type" : "new round",       
        "factory displays" : 
            [
                [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ], 
                [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ], 
                [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ],
                 [
                "<tile color>", 
                "<tile color>",
                "<tile color>",
                "<tile color>"
                 ]
                 ... 
            ]
        }


- server broadcasts to the users the name of the winner 
{"type" : "winner", "name" : "<winner name>"}