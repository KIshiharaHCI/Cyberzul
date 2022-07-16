package cyberzul.network.client;

import cyberzul.model.ClientFactoryDisplay;
import cyberzul.model.ClientTableCenter;
import cyberzul.model.CommonModel;
import cyberzul.model.GameModel;
import cyberzul.model.ModelStrategy;
import cyberzul.model.ModelTile;
import cyberzul.model.Offering;
import cyberzul.model.Player;
import cyberzul.model.events.ConnectedWithServerEvent;
import cyberzul.model.events.GameCanceledEvent;
import cyberzul.model.events.GameFinishedEvent;
import cyberzul.model.events.GameForfeitedEvent;
import cyberzul.model.events.GameNotStartableEvent;
import cyberzul.model.events.GameStartedEvent;
import cyberzul.model.events.IllegalTurnEvent;
import cyberzul.model.events.LoggedInEvent;
import cyberzul.model.events.LoginFailedEvent;
import cyberzul.model.events.NextPlayersTurnEvent;
import cyberzul.model.events.NoValidTurnToMakeEvent;
import cyberzul.model.events.NotYourTurnEvent;
import cyberzul.model.events.PlayerHasChosenTileEvent;
import cyberzul.model.events.RoundFinishedEvent;
import cyberzul.model.events.UserJoinedEvent;
import cyberzul.shared.JsonMessage;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class acts as if it was a GameModel to the view. The view can use all its methods that
 * it would use in the hot seat mode to communicate with the GameModel.
 * Instead of altering the game state directly, this class sends messages to the server and tells
 * the server about the request of the user.
 *
 * <p>The server uses this object (and related objects like the ClientPlayer) to store information
 * about the game state on the local machine of the user, so if the view has to access some
 * information (i.e. the content of the FactoryDisplays), it doesn't have to send a message to the
 * server and request the information, but it already has this information stored locally and ready
 * to use.
 *
 * <p>The server will also keep this data updated.
 */
public class ClientModel extends CommonModel implements ModelStrategy {

  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);
  private ClientNetworkConnection connection;
  private String thisPlayersName;

  /**
   * Create a ClientModel and start a connection with the server.
   */
  public ClientModel() {
    super();
    this.connection = new ClientNetworkConnection(this);
    connection.start();
  }

  @Override
  public void startGame() {
    connection.send(JsonMessage.START_GAME);
  }

  @Override
  public void restartGame() {
    connection.send(JsonMessage.RESTART_GAME);
  }

  @Override
  public void cancelGame() {
    connection.send(JsonMessage.CANCEL_GAME);
  }

  @Override
  public void replacePlayerByAi(String playerName) {
    connection.send(JsonMessage.REPLACE_PLAYER_BY_AI);
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

  private synchronized ClientNetworkConnection getConnection() {
    return connection;
  }

  /**
   * Gets invoked if the client connects with the server. Even before he logs in with his name. He
   * then already gets the information about the names of the players who already joined and saves
   * them in his playerList
   *
   * @param playerNames the names of the players who already joined the server as JSONArray
   */
  public void connected(JSONArray playerNames) {
    try {
      setUpClientPlayersByName(playerNames);
      notifyListeners(new ConnectedWithServerEvent());
    } catch (JSONException jsonException) {
      jsonException.printStackTrace();
    }
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
    playerList.add(new ClientPlayer(thisPlayersName));
    notifyListeners(new LoggedInEvent());
  }

  /**
   * Update the model accordingly when another user joined.
   *
   * @param nickname the player that joined the server.
   */
  public void userJoined(String nickname) {
    playerList.add(new ClientPlayer(nickname));
    notifyListeners(new UserJoinedEvent(nickname));
  }

  /**
   * Notify the subscribed observers that a login attempt has failed.
   */
  public void loginFailed(String message) {
    notifyListeners(new LoginFailedEvent(message));
  }

  /**
   * Update all information in the model that is needed by the view to start showing the game. Then
   * notify the listeners that the game has started.
   */
  public void handleGameStarted(JSONArray offerings, JSONArray playerNames) throws JSONException {
    updateOfferings(offerings);
    // player list gets updated. Just in case. We didn't run in a bug yet, but the integrity of the
    // order of players in the game is much more important than saving a few Strings in a JSON
    // message.
    setUpClientPlayersByName(playerNames);
    indexOfActivePlayer = 0;
    try {
      ClientPlayer startingPlayer = (ClientPlayer) playerList.get(0);
      startingPlayer.setHasStartingPlayerMarker(true);
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
    this.isGameStarted = true;
    notifyListeners(new GameStartedEvent());
  }

  /**
   * Intitializes the data structure that contains the information about each player.
   *
   * @param playerNames the names of the players that have already logged in on the server.
   */
  private void setUpClientPlayersByName(JSONArray playerNames) throws JSONException {
    ArrayList<Player> playerList = new ArrayList<>();
    for (int i = 0; i < playerNames.length(); i++) {
      ClientPlayer clientPlayer = new ClientPlayer(playerNames.getString(i));
      playerList.add(clientPlayer);
    }
    this.playerList = playerList;
  }

  /**
   * Update the Offerings in the Model.
   *
   * @param offerings the offerings of Azul. This information comes directly form the server.
   */
  private void updateOfferings(JSONArray offerings) throws JSONException {
    ArrayList<Offering> returnOfferingsList = new ArrayList<>();

    // update TableCenter
    ArrayList<ModelTile> content = new ArrayList<>();
    JSONArray tableCenterArray = offerings.getJSONArray(0);
    for (int i = 0; i < tableCenterArray.length(); i++) {
      content.add(ModelTile.toTile(tableCenterArray.getString(i)));
    }
    ClientTableCenter clientTableCenter = new ClientTableCenter();
    clientTableCenter.setContent(content);
    returnOfferingsList.add(clientTableCenter);

    // update FactoryDisplays
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

  /**
   * The client has received a message that the next player has to start his turn. So the last
   * player did his turn. That means the information stored on this client's device is outdated and
   * has to be updated. The new, up-to-date information is stored in the JSON object that is sent
   * from the server to the client.
   * Update following information
   * <li>Who is the active player now.</li>
   * <li>How do the FactoryDisplays and TableCenter looks like now.</li>
   * <li>How do the PatternLines and FloorLine of the player who ended his turn look like now.</li>
   *
   * @param object the object containing
   * @throws JSONException if the JSONObject is defective.
   */
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

    notifyListeners(new NextPlayersTurnEvent(nameOfActivePlayer, nameOfPlayerWhoEndedHisTurn));
  }

  /**
   * Update the information about the player that has the starting player marker at the moment.
   *
   * @param indexOfPlayerWhoShouldGetSpm the index of the player with the starting player marker.
   */
  private void setPlayerWithSpm(int indexOfPlayerWhoShouldGetSpm) {
    try {
      for (Player p : playerList) {
        ClientPlayer clientPlayer = (ClientPlayer) p;
        clientPlayer.setHasStartingPlayerMarker(false);
      }
      ClientPlayer playerWhoShouldGetTheSpm =
          (ClientPlayer) playerList.get(indexOfPlayerWhoShouldGetSpm);
      playerWhoShouldGetTheSpm.setHasStartingPlayerMarker(true);
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update the content of the PatternLines of the specified player.
   *
   * @param newPatternLines the up-to-date content of the PatternLines.
   * @param player the player whose PatternLines get updated.
   */
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

  /**
   * Update the content of the wall of the specified player.
   *
   * @param newWallMessage the up-to-date content of the wall.
   * @param player the player whose wall gets updated.
   */
  private void updateWall(JSONArray newWallMessage, ClientPlayer player) {
    try {
      boolean[][] wall = new boolean[newWallMessage.length()][newWallMessage.length()];
      for (int row = 0; row < newWallMessage.length(); row++) {
        for (int col = 0; col < newWallMessage.getJSONArray(row).length(); col++) {
          if (ModelTile.toTile(newWallMessage.getJSONArray(row).getString(col))
              != ModelTile.EMPTY_TILE) {
            wall[row][col] = true;
          }
        }
      }
      player.setWall(wall);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update the number of points that the specified player has right now.
   *
   * @param newPoints number of points.
   * @param player the player whose points get updated.
   */
  private void updatePoints(int newPoints, ClientPlayer player) {
    player.setPoints(newPoints);
  }

  /**
   * Update the content of the FloorLine of the specified player.
   *
   * @param newFloorLine the up-to-date content of the FloorLine of this player.
   * @param player the player whose FloorLine gets updated.
   */
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

  /**
   * Notify the user that it's not his turn, and he has to wait until he can make his next move.
   */
  public void handleNotYourTurn() {
    notifyListeners(new NotYourTurnEvent());
  }

  /**
   * Notify the listeners that the active user has chosen a tile.
   *
   * @param object information about this event.
   */
  public void handlePlayerHasChosenTile(JSONObject object) {
    try {
      notifyListeners(
          new PlayerHasChosenTileEvent(object.getString(JsonMessage.NAME_OF_ACTIVE_PLAYER_FIELD)));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fire an event that tells the player that he has no legal turn left.
   */
  public void handleNoValidTurnToMake() {
    notifyListeners(new NoValidTurnToMakeEvent());
  }

  /**
   * Fire an event that tells the user (who tried to start a game) that this is not possible.
   *
   * @param reason the reason why the user can't start the game.
   */
  public void handleGameNotStartable(String reason) {
    notifyListeners(new GameNotStartableEvent(reason));
  }

  /**
   * One game round has ended and the tiles were put on the wall. Now the data of every player has
   * changed and needs to get updated on the local device. (that is done by this method).
   *
   * <p>Also notifies the view that the round has finished.
   *
   * @param message contains the up-to-date data of the game state and player states.
   */
  public void handleRoundFinished(JSONObject message) {
    try {
      JSONArray players = message.getJSONArray(JsonMessage.PLAYER_FIELD);
      updatePlayers(players);

      setPlayerWithSpm(message.getInt(JsonMessage.INDEX_OF_PLAYER_WITH_SPM));
      notifyListeners(new RoundFinishedEvent());
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * The game has finished. So the last tiling phase ended and all player information that is stored
   * on the device of the client is outdated and has to be updated.
   * That is done by this method.
   *
   * @param message contains the up-to-date data of the game state and player states.
   */
  public void handleGameFinishedEvent(JSONObject message) {
    try {
      JSONArray players = message.getJSONArray(JsonMessage.PLAYER_FIELD);
      updatePlayers(players);
      this.isGameStarted = false;
      notifyListeners(new GameFinishedEvent(message.getString(JsonMessage.NICK_FIELD)));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update the data of all players.
   *
   * @param players the new, up-to-date data of all players.
   * @throws JSONException gets thrown if the JSONArray is defective.
   */
  private void updatePlayers(JSONArray players) throws JSONException {
    for (int i = 0; i < players.length(); i++) {
      JSONObject playerObject = players.getJSONObject(i);
      String playerName = playerObject.getString(JsonMessage.NICK_FIELD);
      ClientPlayer clientPlayer = (ClientPlayer) getPlayerByName(playerName);

      updatePatternLines(playerObject.getJSONArray(JsonMessage.PATTERN_LINES_FIELD), clientPlayer);
      updateFloorLine(playerObject.getJSONArray(JsonMessage.FLOOR_LINE_FIELD), clientPlayer);
      updateWall(playerObject.getJSONArray(JsonMessage.WALL_FIELD), clientPlayer);
      updatePoints(playerObject.getInt(JsonMessage.POINTS_FIELD), clientPlayer);
    }
  }

  /**
   * Get the name with which this client has logged in on the server.
   *
   * @return the name with which this client has logged in on the server.
   */
  public String getPlayerName() {
    return this.thisPlayersName;
  }

  /**
   * Notify the listeners that the turn he tried to do was not a legal game move.
   */
  public void handleIllegalTurn() {
    notifyListeners(new IllegalTurnEvent());
  }

  /**
   * Notify the listeners that the game has been canceled.
   *
   * @param playerWhoCanceledGame the name of the player who canceled the game.
   */
  public void handleGameCanceled(String playerWhoCanceledGame) {
    this.isGameStarted = false;
    notifyListeners(new GameCanceledEvent(playerWhoCanceledGame));
  }

  /**
   * Notify the listeners that a player has forfeited and left the game.
   *
   * @param playerWhoForfeitedTheGame the name of the player who forfeited the game.
   */
  public void handleGameForfeited(String playerWhoForfeitedTheGame) {
    notifyListeners(new GameForfeitedEvent(playerWhoForfeitedTheGame));
  }
}
