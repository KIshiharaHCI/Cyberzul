package azul.team12.model;

import static java.util.Objects.requireNonNull;

import azul.team12.AzulMain;
import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameFinishedEvent;
import azul.team12.model.events.GameForfeitedEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.IllegalTurnEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.NextPlayersTurnEvent;
import azul.team12.model.events.NoValidTurnToMakeEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import azul.team12.model.events.PlayerHasChosenTileEvent;
import azul.team12.model.events.PlayerHasEndedTheGameEvent;
import azul.team12.model.events.RoundFinishedEvent;
import azul.team12.view.board.PatternLines;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameModel {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;

  private final PropertyChangeSupport support;
  private ArrayList<Player> playerList;
  private ArrayList<Offering> offerings;
  private boolean isGameStarted = false;
  private boolean hasGameEnded = false;
  private int indexOfActivePlayer = 0;
  private Offering currentOffering;
  private int currentIndexOfTile;

  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);

  public GameModel() {
    support = new PropertyChangeSupport(this);
    playerList = new ArrayList<>();
    offerings  = new ArrayList<>();
  }

  /**
   * Add a {@link PropertyChangeListener} to the model for getting notified about any changes that
   * are published by this model.
   *
   * @param listener the view that subscribes itself to the model.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

  /**
   * Remove a listener from the model. From then on it will no longer get notified about any events
   * fired by the model.
   *
   * @param listener the view that is to be unsubscribed from the model.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link azul.team12.model.events.GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed.
   *
   * @param event A concrete implementation of {@link azul.team12.model.events.GameEvent}
   */
  private void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  /**
   * A player trys to log in. Fires {@link LoginFailedEvent} if that was not possible.
   *
   * @param nickname the name that the player chose with his login attempt.
   */
  public void loginWithName(String nickname) {
    boolean nicknameFree = true;
    if (playerList.size() >= MAX_PLAYER_NUMBER) {
      notifyListeners(new LoginFailedEvent(LoginFailedEvent.LOBBY_IS_FULL));
    } else {
      for (Player player : playerList) {
        if (player.getName().equals(nickname)) {
          nicknameFree = false;
          notifyListeners(new LoginFailedEvent(LoginFailedEvent.NICKNAME_ALREADY_TAKEN));
        }
      }
      if (nicknameFree) {
        Player newPlayer = new Player(nickname);
        playerList.add(newPlayer);
      }
    }
  }

  /**
   * Tries to start the game. Fires {@link GameNotStartableEvent} if that is
   * not possible.
   */
  public void startGame() {
    if (playerList.size() < MIN_PLAYER_NUMBER) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (isGameStarted) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.GAME_ALREADY_STARTED));
    }
    else{
      setUpOfferings();
      notifyListeners(new GameStartedEvent());
    }
  }

  /**
   * forfeits the game, removes all tiles from the table center, initializes the
   * bag to store used tiles and the bag to draw new tiles, notifies listeners that the game has
   * been forfeit.
   */
  public void forfeitGame() {
    LOGGER.info(getNickOfActivePlayer() + " wants to forfeit the game.");
    GameForfeitedEvent gameForfeitedEvent = new GameForfeitedEvent(getNickOfActivePlayer());
    TableCenter.getInstance().initializeContent();
    BagToStoreUsedTiles.getInstance().initializeContent();
    BagToDrawNewTiles.getInstance().initializeContent();
    isGameStarted = false;
    hasGameEnded = false;
    playerList = new ArrayList<>();
    offerings = new ArrayList<>();
    notifyListeners(gameForfeitedEvent);

  }

  /**
   * Creates the Table Center and as many Factory Displays as needed and saves it in the offerings
   * list.
   */
  private void setUpOfferings(){
    offerings = new ArrayList<>();
    offerings.add(TableCenter.getInstance());
    TableCenter.getInstance().addStartPlayerMarker();
    int numberOfFactoryDisplays = (playerList.size() * 2) + 1;
    for(int i = 0; i < numberOfFactoryDisplays; i++){
      offerings.add(new FactoryDisplay());
    }
  }

  public List<Offering> getOfferings() {
    return (List<Offering>) offerings.clone();
  }

  public List<Offering> getFactoryDisplays(){
    // return the factory displays being the all but the first offering
    return offerings.subList(1, offerings.size());
  }

  public Offering getTableCenter(){
    return TableCenter.getInstance();
  }

  /**
   * Returns the nickname of the player who has to make his turn.
   *
   * @return the nickname of the player who has to make his turn.
   */
  public String getNickOfActivePlayer(){
    return playerList.get(indexOfActivePlayer).getName();
  }

  /**
   * Returns the number of points the player with the specified nickname has.
   *
   * @param nickname name of the player.
   * @return the points this player has.
   */
  public int getPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  /**
   * Return the minus points the player acquired during this round because of Tiles that fell to
   * the flore.
   *
   * @param nickname the name of the player whose minus points we want to know.
   * @return the number of points he already has.
   */
  public int getMinusPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getMinusPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  /**
   * Ends the turn. Notifies listeners that the turn has ended, sets the index of the active
   * player accordingly.
   */
  public void endTurn(){
    boolean roundFinished = checkRoundFinished();
    if (roundFinished) {
      indexOfActivePlayer = giveIndexOfPlayerWithSPM();
      RoundFinishedEvent roundFinishedEvent = new RoundFinishedEvent();
      startTilingPhase();
      if(!hasGameEnded){
        setUpOfferings();
      }
      notifyListeners(roundFinishedEvent);
      } else {
      indexOfActivePlayer = getIndexOfNextPlayer(indexOfActivePlayer);
      //LOGGER.info("Player " + getNickOfActivePlayer() + "s pattern lines: " + getPlayerByName(getNickOfActivePlayer()).getPatterLinesAsString());
    }
    NextPlayersTurnEvent nextPlayersTurnEvent = new NextPlayersTurnEvent(getNickOfActivePlayer());
    notifyListeners(nextPlayersTurnEvent);
  }

  /**
   * Next player is the next player on the list or the first player, if the last active player
   * was the last player on the list.
   *
   * @param indexOfCurrentPlayer index of the current player, most of the time the global
   *                             variable (indexOfActivePlayer)
   * @return the index of the player whose turn it is going to be.
   */
  public int getIndexOfNextPlayer(int indexOfCurrentPlayer) {
    int indexOfNextPlayer;
    if (indexOfActivePlayer == playerList.size() - 1) {
      indexOfNextPlayer = 0;
    } else {
      indexOfNextPlayer = indexOfActivePlayer + 1;
    }
    return indexOfNextPlayer;
  }

  /**
   * Return player by name (given by view).
   *
   * @param nickname the nickname of the player
   * @return the player
   */
  public Player getPlayerByName(String nickname) {
    for(Player player : playerList) {
      if(player.getName().equals(nickname)) {
        return player;
      }
    }
    LOGGER.debug("The model was given a name by view that is not in the playerList.");
    return null;
  }

  /**
   * Returns the nicknames of all players.
   * @return List of nicknames.
   */
  public List<String> getPlayerNamesList() {
    List<String> list = new ArrayList<>();
    for (Player player: playerList) {
      list.add(player.getName());
    }
    return list;
  }
  public int getIndexOfActivePlayer() {
    return indexOfActivePlayer;
  }

  /**
   * informs the view via listeners that it is the next players turn. If the player cannot
   * place the tile on a pattern line, it still informs the model.
   *
   * @param playerName  the player's name
   * @param indexOfTile the index of the tile that was drawn
   * @param offering    the offering (factory display or center of the table)
   */
  public void notifyTileChosen(String playerName, int indexOfTile, Offering offering) {
    boolean thereIsAValidPick = false;
    currentOffering = offering;
    currentIndexOfTile = indexOfTile;
    Player player = getPlayerByName(playerName);
    // check for each line in the pattern lines if there is a valid pick
    for (int line = 0; line < Player.NUMBER_OF_PATTERN_LINES; line++) {
      if (player.isValidPick(line, offering, indexOfTile)) {
        thereIsAValidPick = true;
      }
    }
    // inform listeners if there is a valid pick, who the next player is
    // if not: that there is not valid turn to make
    if (thereIsAValidPick) {
      int indexOfNextPlayer = getIndexOfNextPlayer(indexOfActivePlayer);
      Player nextPlayer = playerList.get(indexOfNextPlayer);
      String nextPlayerNick = nextPlayer.getName();
      PlayerHasChosenTileEvent playerHasChosenTileEvent = new PlayerHasChosenTileEvent(nextPlayerNick);
      notifyListeners(playerHasChosenTileEvent);
    } else {
      NoValidTurnToMakeEvent noValidTurnToMakeEvent = new NoValidTurnToMakeEvent();
      notifyListeners(noValidTurnToMakeEvent);
    }
  }

  /**
   * makes the active player place the tile he/she has chosen on a given pattern line.
   *
   * @param rowOfPatternLine the row of the chosen pattern line
   * @return <code>true</code> if it was a valid pick, <code>false</code> if not
   */
  public boolean makeActivePlayerPlaceTile(int rowOfPatternLine) {
    LOGGER.info(getNickOfActivePlayer() + " tries to place a tile on patter line " + rowOfPatternLine + ".");
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    return activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile);
  }

  /**
   * makes the active player place the tile he/she has chosen directly into the floor line.
   */
  public void tileFallsDown() {
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    LOGGER.info(nickActivePlayer + " tries to place a tile directly into the floor line.");
    if(currentOffering == null){
      notifyListeners(new IllegalTurnEvent());
    }
    else {
      activePlayer.placeTileInFloorLine(currentOffering, currentIndexOfTile);
    }
  }

  /**
   * Checks whether the there is still an offering with a non-empty content.
   *
   * @return <code>true</code> if the round is finished, <code>false</code> if not.
   */
  public boolean checkRoundFinished() {
    for (Offering offering : offerings) {
      // if any of the offerings still has a content, the round is not yet finished
      if (!offering.getContent().isEmpty()) {
        return false;
        }
    }
    return true;
  }

  /**
   * Gives back the index of the player with the Start Player Marker.
   *
   * @return player's index.
   */
  public int giveIndexOfPlayerWithSPM() {
    int index = 0;
    for (Player player : playerList) {
      if (player.hasStartingPlayerMarker()) {
        return index;
      }
      index ++;
    }
    LOGGER.debug("We called giveIndexOfPlayer with Start Player Marker when no player had "
        + "the SPM. Probably this is the case because at the end of the turn noone had the "
        + "SPM.");
    throw new IllegalStateException("We called giveIndexOfPlayer with Start Player Marker when "
        + "no player had the SPM.");
  }

  /**
   * Tell each player to tile the wall and get the points accordingly.
   */
  public void startTilingPhase() {
    hasGameEnded = false;
    for (Player player : playerList) {
      player.tileWallAndGetPoints();
      if (player.hasEndedTheGame()) {
        PlayerHasEndedTheGameEvent playerHasEndedTheGameEvent =
            new PlayerHasEndedTheGameEvent(player.getName());
        notifyListeners(playerHasEndedTheGameEvent);
        hasGameEnded = true;
      }
      // Loop has to finish, because all players have to finish tiling phase
    }
    if (hasGameEnded) {
      String winnerName = getPlayerWithMostPoints();
      GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(winnerName);
      notifyListeners(gameFinishedEvent);
    }
  }

  /**
   * fFnds the player with the most points.
   *
   * @return the name of the player with most points.
   */
  public String getPlayerWithMostPoints() {
    //TODO: What if two players have the same points?
    ArrayList<Integer> playerPoints = new ArrayList<>();
    for (Player player : playerList) {
      playerPoints.add(player.getPoints());
    }
    int highestScore = Collections.max(playerPoints);
    int bestIndex = playerPoints.indexOf(highestScore);
    Player playerWithMostPoints = playerList.get(bestIndex);
    String playerNameOfPlayerWithMostPoints = playerWithMostPoints.getName();
    return playerNameOfPlayerWithMostPoints;
  }

  /**
   * Ranking the players according its points.
   *
   * @return a list of players with points in descending order.
   */
  public List<Player> rankingPlayerWithPoints() {
    List<Player> playerRankingList = playerList;
    Collections.sort(playerRankingList, (o1, o2) -> -Integer.compare(getPoints(o1.getName()), getPoints(o2.getName())));
    return playerRankingList;

  }

  /**
   * gives back the pattern Lines of a given player.
   *
   * @param playerName the name of the player
   * @return the pattern Lines
   */
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getPatternLines();
  }

  /**
   * gives back the floor line of a given player.
   *
    * @param playerName the name of the player
   * @return the floor line
   */
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getFloorLine();
  }


  /**
   * gives back the wall of a given player based on his/her wall represented in booleans.
   *
   * @param playerName the name of the player
   * @return the wall
   */
  public ModelTile[][] getWallOfPlayer(String playerName) {

    Player player = getPlayerByName(playerName);
    ModelTile[][] templateWall = WallBackgroundPattern.getTemplateWall();
    boolean[][] wallAsBools = player.getWall();
    ModelTile[][] playerWall = new ModelTile[5][5];

    for (int row = 0; row < wallAsBools.length; row++) {
      for (int col = 0; col < wallAsBools[row].length; col++) {
        if (wallAsBools[row][col]) {
          playerWall[row][col] = templateWall[row][col];
        } else {
          playerWall[row][col] = ModelTile.EMPTY_TILE;
        }
      }
    }

    return playerWall;

  }


}
