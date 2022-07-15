# CHANGELOG

All notable changes to this project will be documented in this file.

The format is based on [ Keep a Changelog ]( https://keepachangelog.com/en/1.0.0/ ) ,
and this project adheres to [ Semantic Versioning ]( https://semver.org/spec/v2.0.0.html ).

## [Unreleased]

- Add Network Multiplayer Mode.
    - It is possible to play the game.
    - The rules of Azul are validated by the server.
- Design changes to the view (in all modes).
- Display the player boards of the non-active players (in all modes).
- Restarting and forfeiting the game is possible.
- Add a Chat
    - to inform the players of GameEvents.
    - that enables the players to send each other messages.

## [1.0.0] - 2022-07-03

### Added

- Basic Implementation of the hot seat mode.
    - The first version of the view shows the most important parts Azul Game using images of tiles.
    - In it every user has a name.
    - You can start the game via
        - Three test buttons with 2, 3, or 4 players with fixed names.
        - Choosing your own names for 2, 3, or 4 players via the "+ Add Player"-Button and start the
          game afterwards.
    - You see the template wall with to a certain degree transparent tiles.
    - You can choose tiles from factory displays and put them on the pattern lines.
    - The model automatically checks if the player made a valid turn and informs him if it was not.
    - Tiles that have not been put on the pattern lines are placed on the table center
      automatically.
    - Tiles that did not find a place in a pattern line are placed in the floor line automatically.
    - After each turn the player boards are updated on the view.
    - During the round the minus points of the active player are shown.
    - In the tiling phase, the points are updated and shown afterwards.
    - After tiling phase wall is shown with the correct tiles being tiled.
    - After tiling phase a new round is started and manufacturing displays are refilled, the
      starting player marker is added to the table center again.
    - It is possible to play the game until a player wins.
    - When a player wins, the points of the players are compared and his/her name is shown.

- DailyLog.md - containing the daily logs of the team
- README.md
- JSON-Protocol
- Added JUnit tests using JUnit 5.8.1 for the bag classes and the player class

[Unreleased]: https://gitlab2.cip.ifi.lmu.de/sosy-lab/sep-ss-22/team12/-/releases

[1.0.0]: https://gitlab2.cip.ifi.lmu.de/sosy-lab/sep-ss-22/team12/-/releases




