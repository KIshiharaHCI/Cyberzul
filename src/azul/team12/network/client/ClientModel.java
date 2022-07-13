package azul.team12.network.client;

import static java.util.Objects.requireNonNull;

import azul.team12.model.ClientFactoryDisplay;
import azul.team12.model.ClientTableCenter;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.Player;
import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameFinishedEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.LoggedInEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.NextPlayersTurnEvent;
import azul.team12.model.events.NoValidTurnToMakeEvent;
import azul.team12.model.events.NotYourTurnEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import azul.team12.model.events.PlayerHasChosenTileEvent;
import azul.team12.model.events.RoundFinishedEvent;
import azul.team12.shared.JsonMessage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientModel implements Model {

  private final PropertyChangeSupport support;
  private boolean loggedIn;
  private ClientNetworkConnection connection;
  private String thisPlayersName;

  private int indexOfActivePlayer;
  private ArrayList<ClientPlayer> playerList = new ArrayList<>();
  protected ArrayList<Offering> offerings;

  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);

  public ClientModel() {

    loggedIn = false;

    support = new PropertyChangeSupport(this);

    setConnection(new ClientNetworkConnection(this));
  }

  /**
   * Add a {@link PropertyChangeListener} to the model to get notified about any changes that the
   * the model publishes.
   *
   * @param listener the view that subscribes itself to the model.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

  /**
   * Remove a listener from the model. It will then no longer get notified about any events
   * fired by the model.
   *
   * @param listener the view that is to be unsubscribed from the model.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  @Override
  public void startGame() {
    connection.send(JsonMessage.START_GAME);
  }

  @Override
  public void restartGame() {

  }

  @Override
  public void cancelGame() {

  }

  @Override
  public void replaceActivePlayerByAi() {

  }

  @Override
  public void makeAiPlayerMakeMove(String nickOfAiPlayer) {

  }

  @Override
  public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {
    connection.send(JsonMessage.notifyTileChosenMessage(indexOfTile, offeringIndex));
  }

  @Override
  public void makeActivePlayerPlaceTile(int rowOfPatternLine) {
    connection.send(JsonMessage.placeTileInPatternLine(rowOfPatternLine));
  }

  @Override
  public void tileFallsDown() {
    connection.send(JsonMessage.placeTileInFloorLine());
  }

  @Override
  public String getPlayerWithMostPoints() {
    //TODO: What if two players have the same points?
    ArrayList<Integer> playerPoints = new ArrayList<>();
    for (Player player : playerList) {
      playerPoints.add(player.getPoints());
    }
    int highestScore = Collections.max(playerPoints);
    int bestIndex = playerPoints.indexOf(highestScore);
    Player playerWithMostPoints = playerList.get(bestIndex);
    return playerWithMostPoints.getName();
  }

  @Override
  public List<String> rankingPlayerWithPoints() {
    List<String> playerNamesRankingList = getPlayerNamesList();
    Collections.sort(playerNamesRankingList,
        (o1, o2) -> -Integer.compare(getPoints(o1), getPoints(o2)));
    return playerNamesRankingList;
  }

  @Override
  public int getIndexOfActivePlayer() {
    return indexOfActivePlayer;
  }

  @Override
  public int getIndexOfNextPlayer() {
    int indexOfNextPlayer;
    if (checkRoundFinished()) {
      indexOfNextPlayer = this.getIndexOfPlayerWithSpm();
    } else if (indexOfActivePlayer == playerList.size() - 1) {
      indexOfNextPlayer = 0;
    } else {
      indexOfNextPlayer = indexOfActivePlayer + 1;
    }
    return indexOfNextPlayer;
  }

  @Override
  public Player getPlayerByName(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player;
      }
    }
    return null;
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getPatternLines();
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getFloorLine();
  }

  @Override
  public ModelTile[][] getWallOfPlayer(String playerName) {

    Player player = getPlayerByName(playerName);
    ModelTile[][] templateWall = player.getWallPattern().pattern;
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

  @Override
  public List<String> getPlayerNamesList() {
    List<String> list = new ArrayList<>();
    for (Player player : playerList) {
      list.add(player.getName());
    }
    return list;
  }

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed.
   */
  void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  private synchronized ClientNetworkConnection getConnection() {
    return connection;
  }

  /**
   * Add a network connector to this model.
   *
   * @param connection The network connection to be added.
   */
  public synchronized void setConnection(ClientNetworkConnection connection) {
    this.connection = connection;
  }

  public synchronized boolean isLoggedIn() {
    return loggedIn;
  }

  public synchronized void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  /**
   * Send a login request to the server.
   *
   * @param nickname the chosen nickname of the chat participant.
   */
  public void loginWithName(final String nickname) {
    this.thisPlayersName = nickname;
    getConnection().sendLogin(nickname);
  }

  /**
   * Update the model accordingly when a login attempt is successful. This is afterwards published
   * to the subscribed listeners.
   */
  public void loggedIn() {
    setLoggedIn(true);
    notifyListeners(new LoggedInEvent());
  }

  /**
   * Notify the subscribed observers that a login attempt has failed.
   */
  public void loginFailed(String message) {
    setLoggedIn(false);
    notifyListeners(new LoginFailedEvent(message));
  }

  /**
   * Update all information in the model that is needed by the view to start showing the game.
   * Then notify the listeners that the game has started.
   */
  public void handleGameStarted(JSONArray offerings, JSONArray playerNames) throws JSONException {
    updateOfferings(offerings);
    setUpClientPlayersByName(playerNames);
    indexOfActivePlayer = 0;
    playerList.get(0).setHasStartingPlayerMarker(true);
    notifyListeners(new GameStartedEvent());
  }

  /**
   * Intitializes the data structure that contains the information about each player.
   *
   * @param playerNames
   */
  private void setUpClientPlayersByName(JSONArray playerNames) throws JSONException {
    for (int i = 0; i < playerNames.length(); i++) {
      ClientPlayer clientPlayer = new ClientPlayer(playerNames.getString(i));
      playerList.add(clientPlayer);
    }
  }

  @Override
  public List<Offering> getOfferings() {
    return (List<Offering>) offerings.clone();
  }

  @Override
  public int getPoints(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  @Override
  public int getMinusPoints(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player.getMinusPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  @Override
  public String getNickOfActivePlayer() {
    return playerList.get(indexOfActivePlayer).getName();
  }

  /**
   * Update the Offerings in the Model.
   *
   * @param offerings
   */
  private void updateOfferings(JSONArray offerings) throws JSONException {
    ArrayList<Offering> returnOfferingsList = new ArrayList<>();

    //update TableCenter
    ArrayList<ModelTile> content = new ArrayList<>();
    JSONArray tableCenterArray = offerings.getJSONArray(0);
    for (int i = 0; i < tableCenterArray.length(); i++) {
      content.add(ModelTile.toTile(tableCenterArray.getString(i)));
    }
    ClientTableCenter clientTableCenter = new ClientTableCenter();
    clientTableCenter.setContent(content);
    returnOfferingsList.add(clientTableCenter);

    //update FactoryDisplays
    for (int i = 1; i < offerings.length(); i++) {
      content = new ArrayList<>();
      JSONArray factoryDisplayArray = offerings.getJSONArray(i);
      for (int j = 0; j < factoryDisplayArray.length(); j++) {
        content.add(ModelTile.toTile(factoryDisplayArray.getString(j)));
      }
      ClientFactoryDisplay clientFactoryDisplay = new ClientFactoryDisplay();
      clientFactoryDisplay.setContent(content);
      returnOfferingsList.add(clientFactoryDisplay);
    }
    this.offerings = returnOfferingsList;
  }

  @Override
  public int getIndexOfPlayerWithSpm() {
    int index = 0;
    for (Player player : playerList) {
      if (player.hasStartingPlayerMarker()) {
        return index;
      }
      index++;
    }
    throw new IllegalStateException("We called giveIndexOfPlayer with Start Player Marker when "
        + "no player had the SPM.");
  }

  @Override
  public void startTilingPhase() {

  }

  public boolean checkRoundFinished() {
    for (Offering offering : offerings) {
      // if any of the offerings still has a content, the round is not yet finished
      if (!offering.getContent().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public void handleNextPlayersTurn(JSONObject object) throws JSONException {
    String nameOfActivePlayer = object.getString(JsonMessage.NAME_OF_ACTIVE_PLAYER_FIELD);
    List<String> playerNamesList = getPlayerNamesList();
    for (int i = 0; i < playerNamesList.size(); i++) {
      if (playerNamesList.get(i).equals(nameOfActivePlayer)) {
        this.indexOfActivePlayer = i;
        break;
      }
    }

    JSONArray offerings = object.getJSONArray(JsonMessage.OFFERINGS_FIELD);
    this.updateOfferings(offerings);

    String nameOfPlayerWhoEndedHisTurn =
        object.getString(JsonMessage.NAME_OF_PLAYER_WHO_ENDED_HIS_TURN_FIELD);

    ClientPlayer playerWhoEndedHisTurn =
        (ClientPlayer) getPlayerByName(nameOfPlayerWhoEndedHisTurn);

    JSONArray newPatternLinesOfPlayerWhoEndedHisTurn =
        object.getJSONArray(JsonMessage.PATTERN_LINES_FIELD);
    updatePatternLines(newPatternLinesOfPlayerWhoEndedHisTurn, playerWhoEndedHisTurn);

    JSONArray newFloorLineOfPlayerWhoEndedHisTurn =
        object.getJSONArray(JsonMessage.FLOOR_LINE_FIELD);
    updateFloorLine(newFloorLineOfPlayerWhoEndedHisTurn, playerWhoEndedHisTurn);

    setPlayerWithSPM(object.getInt(JsonMessage.INDEX_OF_PLAYER_WITH_SPM));

    notifyListeners(new NextPlayersTurnEvent(nameOfActivePlayer, nameOfPlayerWhoEndedHisTurn));
  }

  private void setPlayerWithSPM(int indexOfPlayerWithSPM){
    for (ClientPlayer p : playerList) {
      p.setHasStartingPlayerMarker(false);
    }
    playerList.get(indexOfPlayerWithSPM).setHasStartingPlayerMarker(true);
  }

  private void updatePatternLines(JSONArray newPatternLines, ClientPlayer player) {
    try {
      ModelTile[][] patternLines =
          new ModelTile[newPatternLines.length()][newPatternLines.length()];
      for (int row = 0; row < newPatternLines.length(); row++) {
        for (int col = 0; col < newPatternLines.getJSONArray(row).length(); col++) {
          patternLines[row][col] =
              ModelTile.toTile(newPatternLines.getJSONArray(row).getString(col));
        }
      }
      player.setPatternLines(patternLines);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void updateWall(JSONArray newWallMessage, ClientPlayer player) {
    try {
      boolean[][] wall =
          new boolean[newWallMessage.length()][newWallMessage.length()];
      for (int row = 0; row < newWallMessage.length(); row++) {
        for (int col = 0; col < newWallMessage.getJSONArray(row).length(); col++) {
          if (ModelTile.toTile(newWallMessage.getJSONArray(row).getString(col)) != ModelTile.EMPTY_TILE) {
            wall[row][col] = true;
          }
        }
      }
      player.setWall(wall);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void updatePoints(int newPoints, ClientPlayer player){
    player.setPoints(newPoints);
  }

  private void updateFloorLine(JSONArray newFloorLine, ClientPlayer player) {
    try {
      ArrayList<ModelTile> floorLine = new ArrayList<>();
      for (int i = 0; i < newFloorLine.length(); i++) {
        floorLine.add(ModelTile.toTile(newFloorLine.getString(i)));
      }
      player.setFloorLine(floorLine);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void handleNotYourTurn() {
    notifyListeners(new NotYourTurnEvent());
  }

  public void handlePlayerHasChosenTile(JSONObject object) {
    try {
      notifyListeners(
          new PlayerHasChosenTileEvent(object.getString(JsonMessage.NAME_OF_ACTIVE_PLAYER_FIELD)));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void handleNoValidTurnToMake() {
    notifyListeners(new NoValidTurnToMakeEvent());
  }

  public void handleGameNotStartable(String reason) {
    notifyListeners(new GameNotStartableEvent(reason));
  }

  public void handleRoundFinished(JSONObject message) {
    try {
      JSONArray players = message.getJSONArray(JsonMessage.PLAYER_FIELD);
      updatePlayers(players);

      setPlayerWithSPM(message.getInt(JsonMessage.INDEX_OF_PLAYER_WITH_SPM));
      notifyListeners(new RoundFinishedEvent());
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void handleGameFinishedEvent(JSONObject message){
    try {
      JSONArray players = message.getJSONArray(JsonMessage.PLAYER_FIELD);
      updatePlayers(players);
      notifyListeners(new GameFinishedEvent(message.getString(JsonMessage.NICK_FIELD)));
    }
    catch(JSONException e){
      e.printStackTrace();
    }
  }

  private void updatePlayers(JSONArray players) throws JSONException{
    for (int i = 0; i < players.length(); i++) {
      JSONObject playerObject = players.getJSONObject(i);
      String playerName = playerObject.getString(JsonMessage.NICK_FIELD);
      ClientPlayer clientPlayer = (ClientPlayer) getPlayerByName(playerName);

      updatePatternLines(playerObject.getJSONArray(JsonMessage.PATTERN_LINES_FIELD), clientPlayer);
      updateFloorLine(playerObject.getJSONArray(JsonMessage.FLOOR_LINE_FIELD), clientPlayer);
      updateWall(playerObject.getJSONArray(JsonMessage.WALL_FIELD),clientPlayer);
      updatePoints(playerObject.getInt(JsonMessage.POINTS_FIELD),clientPlayer);
    }
  }
}


