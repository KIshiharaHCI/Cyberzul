package azul.team12.model;

import azul.team12.model.events.GameFinishedEvent;
import azul.team12.model.events.GameForfeitedEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.NextPlayersTurnEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import azul.team12.model.events.PlayerHasEndedTheGameEvent;
import azul.team12.model.events.RoundFinishedEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public interface Model {

  /**
   * Add a {@link PropertyChangeListener} to the model for getting notified about any changes that
   * are published by this model.
   *
   * @param listener the view that subscribes itself to the model.
   */

  void addPropertyChangeListener(PropertyChangeListener listener);
  /**
   * Remove a listener from the model. From then on it will no longer get notified about any events
   * fired by the model.
   *
   * @param listener the view that is to be unsubscribed from the model.
   */
  void removePropertyChangeListener(PropertyChangeListener listener);

  /**
   * A player trys to log in. Fires {@link LoginFailedEvent} if that was not possible.
   *
   * @param nickname the name that the player chose with his login attempt.
   */
  void loginWithName(String nickname);

  /**
   * Tries to start the game. Fires {@link GameNotStartableEvent} if that is
   * not possible, fires {@link GameStartedEvent} if it was.
   */
  public void startGame();

  /**
   * Forfeits the game, fires {@link GameForfeitedEvent}, removes all tiles from the table center,
   * initializes the bag to store used tiles and the bag to draw new tiles from scratch,
   * notifies listeners that the game has been forfeit.
   */
  void forfeitGame();

  /**
   * Ends the turn. Fires {@link RoundFinishedEvent} if the round has finished,
   * fires {@link NextPlayersTurnEvent}, sets up the offerings new, if the game
   * has ended and sets up the index of active player to be the index of the next
   * player.
   */
  void endTurn();

  /**
   * Informs the view via listeners that it is the next players turn. If the player cannot
   * place the tile on a pattern line, it still informs the model.
   *
   * @param playerName    the player's name
   * @param indexOfTile   the index of the tile that was drawn
   * @param offeringIndex the offering (factory display or center of the table)
   */
  void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex);

  /**
   * Makes the active player place the tile he/she has chosen on a given pattern line.
   *
   * @param rowOfPatternLine the row of the chosen pattern line
   * @return <code>true</code> if it was a valid pick, <code>false</code> if not
   */
  boolean makeActivePlayerPlaceTile(int rowOfPatternLine);

  /**
   * Makes the active player place the tile he/she has chosen directly into the floor line.
   */
  void tileFallsDown();

  /**
   * Checks whether there is still an offering with a non-empty content.
   *
   * @return <code>true</code> if the round is finished, <code>false</code> if not.
   */
  boolean checkRoundFinished();

  /**
   * Gives back the index of the player with the Start Player Marker.
   *
   * @return player's index.
   */
  int getIndexOfPlayerWithSPM();

  /**
   * Tell each player to tile the wall and get the points accordingly.
   * Fires {@link PlayerHasEndedTheGameEvent} if a player has ended the game in this
   * tiling phase, fires {@link GameFinishedEvent} at the end of this tiling phase in
   * which someone has ended the game.
   */
  void startTilingPhase();

  /**
   * Finds the player with the most points.
   *
   * @return the name of the player with most points.
   */
  String getPlayerWithMostPoints();

  /**
   * Ranking the players according its points.
   *
   * @return a list of players with points in descending order.
   */
  List<Player> rankingPlayerWithPoints();

  /**
   * Next player is the next player on the list or the first player, if the last active player
   * was the last player on the list; or the player with the SPM if round is finished.
   *
   * @return the index of the player whose turn it is going to be.
   */
  int getIndexOfNextPlayer();

  /**
   * Return player by name (given by view).
   *
   * @param nickname the nickname of the player
   * @return the player
   */
   Player getPlayerByName(String nickname);

  /**
   * gives back the pattern Lines of a given player.
   *
   * @param playerName the name of the player
   * @return the pattern Lines
   */
  ModelTile[][] getPatternLinesOfPlayer(String playerName);

  /**
   * gives back the floor line of a given player.
   *
   * @param playerName the name of the player
   * @return the floor line
   */
  List<ModelTile> getFloorLineOfPlayer(String playerName);

  /**
   * gives back the wall of a given player based on his/her wall represented in booleans.
   *
   * @param playerName the name of the player
   * @return the wall
   */
  ModelTile[][] getWallOfPlayer(String playerName);

  /**
   * Returns the nicknames of all players.
   * @return list of nicknames.
   */
  List<String> getPlayerNamesList();

  List<Offering> getOfferings();

  int getIndexOfActivePlayer();

  /**
   * Returns the number of points the player with the specified nickname has.
   *
   * @param nickname name of the player.
   * @return the points this player has.
   */
  int getPoints(String nickname);

  /**
   * Return the minus points the player acquired during this round because of Tiles that fell to
   * the flore. Fires {@link PlayerDoesNotExistEvent} if one tried to get the points of
   * a player that doesn't exist.
   *
   * @param nickname the name of the player whose minus points we want to know.
   * @return the number of points he already has.
   */
  int getMinusPoints(String nickname);

  List<Offering> getFactoryDisplays();

  Offering getTableCenter();

  /**
   * Returns the nickname of the player who has to make his turn.
   *
   * @return the nickname of the player who has to make his turn.
   */
  public String getNickOfActivePlayer();

}
