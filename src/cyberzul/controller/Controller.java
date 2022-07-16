package cyberzul.controller;

import cyberzul.model.ModelTile;
import cyberzul.model.Offering;
import java.util.List;

/**
 * //TODO: The information about the implementation insiede the model gets deleted later on.
 */
public interface Controller {

  /**
   * Try to create a new player with a name. Model informs the view via a notifyListener Event, if
   * that was successful.
   *
   * @param name the name of the player.
   */
  void addPlayer(String name);

  /**
   * Signals the model that all players have entered their name. The model should then initialize
   * everything and starts the game. Players, Bags.
   */
  void startGame();

  /**
   * Signals the model that the previous game should be restarted with the same players. The model
   * initializes everything.
   */
  void restartGame();

  /**
   * Gives the view the information about the manufacturing plates and the table center. Tiles are
   * saved in the Bags as ArrayList.
   *
   * @return the offerings (0 being the table center, the other offerings the factory displays)
   */
  List<Offering> getOfferings();

  /**
   * Returns the Name of the player who has to make his turn.
   *
   * @return name of active player.
   */
  String getNickOfActivePlayer();

  /**
   * Returns the Name of the next player about to make their turn.
   *
   * @return name of next player.
   */
  String getNickOfNextPlayer();

  /**
   * Return the points that the player has.
   *
   * @param playerName the name of the player whose points we want to know.
   * @return the number of points he already has.
   */
  int getPoints(String playerName);

  /**
   * Return the minus points the player acquired during this round because of Tiles that fell to the
   * flore.
   *
   * @param playerName the name of the player whose minus points we want to know.
   * @return the number of points he already has.
   */
  int getMinusPoints(String playerName);

  /**
   * Informs the model that a player chose a tile from a manufacturing plate or from the table
   * center. The model should inform the view with an error message via notify listeners, if the
   * player can't place it on a patter line.
   *
   * @param playerName    the name of the player who makes his move.
   * @param indexOfTile   which of the tiles on the Offering was clicked on.
   * @param offeringIndex index of the offering where the tile is chosen from.
   */
  void chooseTileFrom(String playerName, int indexOfTile, int offeringIndex);

  /**
   * Informs the model on which pattern row the player chose to place his tile.
   *
   * @param rowOfPatternLine the row of the pattern line selected
   */
  void placeTileAtPatternLine(int rowOfPatternLine);

  /**
   * Informs the model that the user has put the tiles of a chosen offering directly to the floor
   * lines.
   */
  void placeTileAtFloorLine();

  /**
   * Returns a list with the nicknames of all players.
   *
   * @return a list with nicknames.
   */
  List<String> getPlayerNamesList();

  /**
   * gives back the pattern line of a given player.
   *
   * @param playerName the name of the player
   * @return the pattern lines
   */
  ModelTile[][] getPatternLinesOfPlayer(String playerName);

  /**
   * gives back the floor line of a given player.
   *
   * @param playerName the name of the player
   * @return the pattern line
   */
  List<ModelTile> getFloorLineOfPlayer(String playerName);

  /**
   * gives back the wall of a given player.
   *
   * @param playerName the name of the player
   * @return the wall as tiles
   */
  ModelTile[][] getWallOfPlayerAsTiles(String playerName);

  /**
   * get the template wall of tiles for the wall so that we need just booleans to show it.
   *
   * @return template wall of tiles.
   */
  ModelTile[][] getTemplateWall();

  /**
   * Rank the players according to their points in descending order.
   *
   * @return A list of players' name.
   */
  List<String> rankingPlayerWithPoints();

  /**
   * tells the model that a given player forfeit the game. Makes the player be replaced by an AI.
   *
   * @param playerName the name of the player to be replaced.
   */
  void replacePlayerByAi(String playerName);

  /**
   * tells the model that a given player wants to cancel the game. Makes the game end.
   */
  void cancelGameForAllPlayers();

  /**
   * Informs if the game has already started.
   *
   * @return <code>true</code> if the game already started. <code>false</code> else.
   */
  boolean isGameStarted();
}
