package azul.team12.model;

import static java.util.Objects.requireNonNull;

import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameFinishedEvent;
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
import java.util.Collections;
import java.util.List;

public class GameModel {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;

  private final PropertyChangeSupport support;
  private ArrayList<Player> playerList;
  //TODO: @Nils please check what I did: Ich habe die factoryDisplays und die Tischmitte in
  // einem ArrayList<Offering> zusammengefasst. Das hat keine Auswirkungen für die View
  // wir geben einfach index 0 für die Tischmitte zurück, aber es war praktisch für die weitere
  // Implementierung - ändere es jederzeit zurück. Außerdem glaube ich, dass uns dadurch die
  // View gar nicht mehr das ganze Offering sondern nur noch einen Index zurückgeben muss.
  // Das habe ich jetzt aber noch nicht geändert. Die Variable unten "currentOffering" könnte dann
  // durch eine Variable "indexOfCurrentOffering" ersetzt werden. Mir gefällt diese Lösung, aber
  // vielleicht übersehe ich etwas.
  private ArrayList<Offering> offerings;
  private boolean isGameStarted = false;
  private boolean hasGameEnded = false;
  private int indexOfActivePlayer = 0;
  private Offering currentOffering;
  private int currentIndexOfTile;


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
   * Creates the Table Center and as many Factory Displays as needed and saves it in the offerings
   * list.
   */
  private void setUpOfferings(){
    offerings = new ArrayList<>();
    offerings.add(TableCenter.getInstance());
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
      System.out.println("Player " + getNickOfActivePlayer() + "s pattern lines: " + getPlayerByName(getNickOfActivePlayer()).getPatterLinesAsString());
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
    System.out.println("To be log - given name by view that is not in the playerList.");
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
   * make the active player place the tile he/she has chosen.
   *
   * @param rowOfPatternLine the row of the chosen pattern line
   * @return <code>true</code> if it was a valid pick, <code>false</code> if not
   */
  public boolean makeActivePlayerPlaceTile(int rowOfPatternLine) {
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    return activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile);
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
    //TODO: Marco - when Logger works, log something.
    System.out.println("We called giveIndexOfPlayer with Start Player Marker when no player had "
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
   * Order and save the players with its points.
   * @return a list of players with points in descending order.
   */
  public List<String> rankingPlayerWithPoints() {
    List<String> nameListOfPlayers = new ArrayList<>();
    Collections.sort(playerList, (o1, o2) -> -Integer.compare(o1.getPoints(), o2.getPoints()));
    for (Player player : playerList) {
      nameListOfPlayers.add(player.getName());

    }
    System.out.println(nameListOfPlayers);
    return nameListOfPlayers;
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

  //TODO: @Marco implement it
  /**
   * Takes in the list of players and gives back a list of player names and their respective points
   * in the order of the points they have collected.
   *
   * @return A List of Players in the order of the points the have currently.
   */
  /*
  public ArrayList<????> getPlayerNamesAndPointsInOrderOfPoints(ArrayList<Player> playerList) {

  }
  */

}
