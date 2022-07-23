package cyberzul.network.client;

import cyberzul.model.ClientFactoryDisplay;
import cyberzul.model.ClientTableCenter;
import cyberzul.model.CommonModel;
import cyberzul.model.GameModel;
import cyberzul.model.ModelStrategy;
import cyberzul.model.ModelTile;
import cyberzul.model.Offering;
import cyberzul.model.Player;
import cyberzul.model.events.BulletModeChangedEvent;
import cyberzul.model.events.ChatMessageRemovedEvent;
import cyberzul.model.events.ConnectedWithServerEvent;
import cyberzul.model.events.GameCanceledEvent;
import cyberzul.model.events.GameFinishedEvent;
import cyberzul.model.events.GameForfeitedEvent;
import cyberzul.model.events.GameNotStartableEvent;
import cyberzul.model.events.GameStartedEvent;
import cyberzul.model.events.IllegalTurnEvent;
import cyberzul.model.events.InvalidIpv4AddressEvent;
import cyberzul.model.events.LoggedInEvent;
import cyberzul.model.events.LoginFailedEvent;
import cyberzul.model.events.NextPlayersTurnEvent;
import cyberzul.model.events.NoValidTurnToMakeEvent;
import cyberzul.model.events.NotYourTurnEvent;
import cyberzul.model.events.PlayerAddedMessageEvent;
import cyberzul.model.events.PlayerDisconnectedEvent;
import cyberzul.model.events.PlayerHas5TilesInArowEvent;
import cyberzul.model.events.PlayerHasChosenTileEvent;
import cyberzul.model.events.PlayerJoinedChatEvent;
import cyberzul.model.events.RoundFinishedEvent;
import cyberzul.model.events.TooManyConnectionAttemptsEvent;
import cyberzul.model.events.UserJoinedEvent;
import cyberzul.model.events.YouDisconnectedEvent;
import cyberzul.network.client.messages.Message;
import cyberzul.network.client.messages.PlayerForfeitedMessage;
import cyberzul.network.client.messages.PlayerJoinedChatMessage;
import cyberzul.network.client.messages.PlayerNeedHelpMessage;
import cyberzul.network.client.messages.PlayerTextMessage;
import cyberzul.network.client.messages.UserLeftMessage;
import cyberzul.network.shared.JsonMessage;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
  private static final int MAX_LENGTH = 10;
  private final List<Message> playerMessages;
  private ClientNetworkConnection connection;
  private String thisPlayersName;

  /**
   * Create a ClientModel and start a connection with the server.
   *
   * @param listenerList the Listeners that tried to connect with the ModelStrategyChooser before
   *                     the ClientModel was created.
   * @param ipAddress    the IPv4 address of the server with which the ClientModel should connect,
   *                     encoded as hex String.
   */
  public ClientModel(List<PropertyChangeListener> listenerList, String ipAddress) {
    super(listenerList);

    setConnection(ipAddress);
    playerMessages = Collections.synchronizedList(new ArrayList<>());
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
    connection.send(JsonMessage.REPLACE_THIS_PLAYER_BY_AI);
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
  public void startSinglePlayerMode(int numberOfPlayers) { /*this method is
  only needed in GameModel */
  }

  private synchronized ClientNetworkConnection getConnection() {
    return connection;
  }

  /**
   * Sets up the connection with a given IP-address.
   *
   * @param ipAddressInHex the IP-address of the server, encoded as hex String.
   */
  public void setConnection(String ipAddressInHex) {
    try {
      //split ipAddressInHex into Strings of length 2.
      String[] ipAddressArray = ipAddressInHex.split("(?<=\\G.{" + 2 + "})");

      //parse the String array to a byte array
      byte[] host = new byte[ipAddressArray.length];
      for (int i = 0; i < ipAddressArray.length; i++) {
        //The hex in the String is unsigned. The long is also unsigned, but should overflow to the
        //correct value in int.
        int partOfTheAddress = (int) Long.parseLong(ipAddressArray[i], 16);
        host[i] = (byte) partOfTheAddress;
      }

      //create the ClientNetworkConnection.
      this.connection =
          new ClientNetworkConnection(this, host);
      connection.start();
    } catch (NumberFormatException e) {
      notifyListeners(new InvalidIpv4AddressEvent());
    }
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
    notifyListeners(new PlayerJoinedChatEvent(new PlayerJoinedChatMessage(nickname)));
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
    notifyListeners(new GameStartedEvent(playerList.get(0).getName()));
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
   * @param player          the player whose PatternLines get updated.
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
   * @param player         the player whose wall gets updated.
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
   * @param player    the player whose points get updated.
   */
  private void updatePoints(int newPoints, ClientPlayer player) {
    player.setPoints(newPoints);
  }

  /**
   * Update the content of the FloorLine of the specified player.
   *
   * @param newFloorLine the up-to-date content of the FloorLine of this player.
   * @param player       the player whose FloorLine gets updated.
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
   * Add a status-update entry "Player joined" to the list of chat entries.
   * Used by the network layer to update the model accordingly.
   *
   * @param nickname The name of the newly joined user.
   */
  public void playerJoinedChat(String nickname) {
    addChatEntry(new PlayerJoinedChatMessage(nickname));
    notifyListeners(new PlayerJoinedChatEvent(new PlayerJoinedChatMessage(nickname)));
  }

  /**
   * Inform the listeners that this player left the game before it ended.
   *
   * @param nickname The name of the player who left the game.
   */
  public void playerLeftBeforeGameStarted(final String nickname) {
    addChatEntry(new PlayerForfeitedMessage(nickname));
    notifyListeners(new GameForfeitedEvent(nickname));
  }

  /**
   * Informs the listeners that this player disconnected from the server before the game even
   * started.
   *
   * @param nickname the nickname of the player who disconnected from the server.
   */
  public void playerLeft(String nickname) {
    addChatEntry(new UserLeftMessage(nickname));
    notifyListeners(new PlayerDisconnectedEvent(nickname));
  }


  /**
   * Send a chat-message to the server that is to be broadcasted to the other chat participants.
   *
   * @param message The message to be broadcasted.
   */
  public void postChatMessage(String message) {
    PlayerTextMessage chatMessage = new PlayerTextMessage(thisPlayersName, new Date(), message);
    addChatEntry(chatMessage);
    getConnection().playerSendMessage(chatMessage);
  }


  /**
   * Add a new {@link Message} to the list of managed messages. Only the latest 100 messages
   * get stored in the collection; the others get dismissed afterwards. The subscribed views get
   * updated by fired {@link PlayerAddedMessageEvent} and {@link ChatMessageRemovedEvent}.
   *
   * @param message The message added to the collection of managed entries.
   */
  private void addChatEntry(Message message) {
    while (playerMessages.size() > MAX_LENGTH) {
      Message removed = playerMessages.remove(0);
      notifyListeners(new ChatMessageRemovedEvent(removed));
    }

    playerMessages.add(message);
    notifyListeners(new PlayerAddedMessageEvent(message));
  }

  /**
   * Return a list of all chat-message-entries, including both user-message entries and
   * status-update entries in the chat.
   *
   * @return a copy of a sorted list containing the entries of the chat.
   */
  public List<Message> getMessages() {
    return new ArrayList<>(playerMessages);
  }

  /**
   * Add a text message to the list of chat entries.
   * Used by the network layer to update the model accordingly.
   *
   * @param nickname The name of the chat participants that has sent this message.
   * @param date     The date when the chat message was sent.
   * @param content  The actual content (text) that the participant had sent.
   */
  public void addTextMessage(String nickname, Date date, String content) {
    PlayerTextMessage message = new PlayerTextMessage(nickname, date, content);
    addChatEntry(message);
  }


  /**
   * Notify the listeners that a player has forfeited and left the game.
   *
   * @param playerWhoForfeitedTheGame the name of the player who forfeited the game.
   */
  public void handleGameForfeited(String playerWhoForfeitedTheGame) {
    getPlayerByName(playerWhoForfeitedTheGame).setName("AI-" + playerWhoForfeitedTheGame);
    notifyListeners(new GameForfeitedEvent(playerWhoForfeitedTheGame));
  }

  /**
   * Add a message without time stamps.
   *
   * <p>@param content the message that should be added.
   */
  public void addTextMessageWithoutTimeStamp(String content) {
    System.out.println(content);
    addChatEntry(new PlayerNeedHelpMessage(content));
  }

  /**
   * Notifies the listeners that a player has 5 tiles in a row.
   *
   * @param nickname the player who has 5 tiles in a row.
   */
  public void handlePlayerHas5TilesInArow(String nickname) {
    notifyListeners(new PlayerHas5TilesInArowEvent(nickname));
  }

  /**
   * Inform the listeners that the client got disconnected from the server.
   */
  public void youGotDisconnected() {
    notifyListeners(new YouDisconnectedEvent());
  }

  @Override
  public void setBulletMode(boolean bulletMode) {
    JSONObject message = JsonMessage.createMessageOfType(JsonMessage.BULLET_MODE);
    try {
      message.put(JsonMessage.IS_BULLET_MODE_FIELD, bulletMode);
      connection.send(message);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fires to the listeners that the bullet mode is triggered on or of by another player.
   *
   * @param object the message that was sent by the server.
   */
  public void handleBulletModeChangedEvent(JSONObject object) {
    try {
      boolean isBulletModeActivated = object.getBoolean(JsonMessage.IS_BULLET_MODE_FIELD);
      notifyListeners(
          new BulletModeChangedEvent(isBulletModeActivated));
      isBulletMode = isBulletModeActivated;
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public void stop(){

  }

  public void handleTooManyConnectionAttempts() {
    notifyListeners(new TooManyConnectionAttemptsEvent());
  }
}