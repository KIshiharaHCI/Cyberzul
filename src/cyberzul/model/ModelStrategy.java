package cyberzul.model;

import cyberzul.model.events.GameForfeitedEvent;
import cyberzul.model.events.GameNotStartableEvent;
import cyberzul.model.events.GameStartedEvent;
import cyberzul.model.events.LoginFailedEvent;
import cyberzul.model.events.PlayerDoesNotExistEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * This Interface is part of the Strategy Design Pattern. The model behaves in a different way
 * depending on the view input. Since the behaviour of the model is only determined at runtime,
 * the ModelStrategyChooser has a field of type ModelStrategy, that then holds the object with the
 * required behaviour (i.e. GameModel or ClientModel).
 */
public interface ModelStrategy {

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
   * Tries to start the game. Fires {@link GameNotStartableEvent} if that is not possible, fires
   * {@link GameStartedEvent} if it was.
   */
  void startGame();

  /**
   * sets up the offerings new, deletes all entries from pattern lines and the floor line of all
   * players, fires {@link GameStartedEvent}, makes all players non-AI-players and sets their points
   * to zero.
   */
  void restartGame();

  /**
   * Forfeits the game, fires {@link GameForfeitedEvent}, removes all tiles from the table center,
   * initializes the bag to store used tiles and the bag to draw new tiles from scratch, notifies
   * listeners that the game has been forfeit.
   */
  void cancelGame();

  /**
   * forfeits the game, sets the player who forfeited to be an AI-Player and makes him/her do the
   * next move.
   *
   * @param playerName the name of the player to be replaced.
   */
  void replacePlayerByAi(String playerName);

  /**
   * gives back the name of the player of the given client model or the name of the active player in
   * hot seat mode.
   *
   * @return a String of the respective players name.
   */
  String getPlayerName();

  /**
   * Informs the view via listeners that it is the next players turn. If the player cannot place the
   * tile on a pattern line, it still informs the model.
   *
   * @param playerName    the player's name
   * @param indexOfTile   the index of the tile that was drawn
   * @param offeringIndex the offering (factory display or center of the table)
   */
  void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex);

  /**
   * Makes the active player place the tile he/she has chosen on a given pattern line.
   *
   * @param rowOfPatternLine the row of the chosen pattern line.
   */
  void makeActivePlayerPlaceTile(int rowOfPatternLine);

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
  int getIndexOfPlayerWithSpm();

  /**
   * Finds the player with the most points at the end of a game. In the case of a tie,
   * the tied player with more complete horizontal lines wins the game. If that does not
   * break the tie, the win will be shared.
   *
   * @return the win message with the winning player(s) name(s).
   */
  String getWinningMessage();

  /**
   * Ranking the players according its points.
   *
   * @return a list of players with points in descending order.
   */
  List<String> rankingPlayerWithPoints();

  /**
   * The active player is the player who has to do his turn in this round.
   *
   * @return the index of the player whose turn is now.
   */
  int getIndexOfActivePlayer();

  /**
   * Next player is the next player on the list or the first player, if the last active player was
   * the last player on the list; or the player with the SPM if round is finished.
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
   *
   * @return the nicknames as strings.
   */
  List<String> getPlayerNamesList();

  /**
   * Returns the list of offerings.
   *
   * @return the offerings.
   */
  List<Offering> getOfferings();

  /**
   * Returns the number of points the player with the specified nickname has.
   *
   * @param nickname name of the player.
   * @return the points this player has.
   */
  int getPoints(String nickname);

  /**
   * Return the minus points the player acquired during this round because of Tiles that fell to the
   * flore. Fires {@link PlayerDoesNotExistEvent} if one tried to get the points of a player that
   * doesn't exist.
   *
   * @param nickname the name of the player whose minus points we want to know.
   * @return the number of points he already has.
   */
  int getMinusPoints(String nickname);

  /**
   * Returns the nickname of the player who has to make his turn.
   *
   * @return the nickname of the player who has to make his turn.
   */
  String getNickOfActivePlayer();

  /**
   * Informs if the game has already started.
   *
   * @return <code>true</code> if the game already started. <code>false</code> else.
   */
  boolean isGameStarted();

  /**
   * Starts a new Game with all AI-Players but the player who started the game.
   *
   * @param numberOfAiPlayers the number of players.
   */
  void startSinglePlayerMode(int numberOfAiPlayers);

  /**
   * The players should make their moves within a certain time span. Starts the timer and
   * if it is not cancelled by the player making a move before, will make the AI make a move
   * for it.
   *
   * @param playerName the name of the player.
   */
  void startTimerForPlayer(String playerName);

}
