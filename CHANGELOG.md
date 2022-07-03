# CHANGELOG

All notable changes to this project will be documented in this file. 
The reader will find the file 

The format is based on [ Keep a Changelog ]( https://keepachangelog.com/en/1.0.0/ ) ,
and this project adheres to [ Semantic Versioning ]( https://semver.org/spec/v2.0.0.html ).


## [Unreleased]
- Finished local hot seat mode
  - Show the minus points of the given player during the round. 
  - Show the points of the player. 
  - Update all player boards on the view. 
  - Show wall after tiling phase.
  - Start new round. 
  - It is possible to play the game until a player wins. 
- Network Multiplayer Mode.
- The rules of Azul are validated by the server.
- Restarting and forfeiting the game is possible.

## [1.0.0] - 2022-07-03 
### Added
- Basic Implementation of the hot seat mode.
  - The model for the basic implementation of the HotSeat Mode is complete. 
  - The first version of the view shows the most important parts Azul Game using images of tiles. 
  - In it every user has a name.
  - You can start the game via 
    - Three test buttons with 2, 3, or 4 players with fixed names. 
    - Choosing your own names for 2, 3, or 4 players via the "+ Add Player"-Button and start the game afterwards. 
  - Choose tiles from factory displays and put them on the pattern lines. 
    - The model automatically checks if the player made a valid turn and informs him if it was not. 
    - Tiles that have not been put on the pattern lines are placed on the table center. 

- DailyLog.md - containing the daily logs of the team
- README.md 
- JSON-Protocol

### Changed
- Nothing

[Unreleased]: https://gitlab2.cip.ifi.lmu.de/sosy-lab/sep-ss-22/team12/-/releases 
[1.0.1]: 




