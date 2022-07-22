package cyberzul.network.server;

import static java.util.Objects.requireNonNull;

import cyberzul.controller.Controller;
import cyberzul.model.GameModel;
import cyberzul.model.Model;
import cyberzul.model.events.GameNotStartableEvent;
import cyberzul.model.events.LoginFailedEvent;
import cyberzul.network.shared.JsonMessage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that stores all relevant information for a single connected client. It contains both
 * personal information related to the client such as the username as well as a reader and writer
 * object that allows this specific client to exchange messages with the server.
 */
public class ClientMessageHandler implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(ClientMessageHandler.class);
  private final ServerNetworkConnection serverConnection;
  private final Socket socket;
  private final BufferedReader reader;
  private final BufferedWriter writer;
  private final Controller controller;
  private final Model model;
  private String nickname;
  private static final int MAX_LENGTH_OF_PLAYER_NAMES = 15;


  /**
   * Set up a new {@link ClientMessageHandler} that deals with incoming and outgoing message for a
   * single connected client. The specific objects are created from the given socket.
   *
   * @param connection The connection handler that manages all the connected clients
   * @param socket     The specific socket that belongs to this client
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  //this class needs these references to these mutable objects.
  public ClientMessageHandler(ServerNetworkConnection connection, Socket socket,
                              Controller controller, Model model)
      throws IOException {
    this.controller = controller;
    this.model = model;
    serverConnection = requireNonNull(connection);
    this.socket = requireNonNull(socket);
    reader =
        new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    writer = new BufferedWriter(
        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

    sendConnectedMessage();
  }

  /**
   * Sends a message to the client that tells it that it connected and also who already logged in to
   * the server.
   */
  private void sendConnectedMessage() {
    try {
      JSONObject connectedMessage = JsonMessage.createMessageOfType(JsonMessage.CONNECTED);
      connectedMessage.put(JsonMessage.PLAYER_NAMES_FIELD,
          JsonMessage.parsePlayerNamesToJsonArray(model.getPlayerNamesList()));
      send(connectedMessage);
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
        String line;
        line = reader.readLine();
        if (line == null) {
          break;
        }
        JSONObject object = new JSONObject(line);
        handleMessage(object);
      }
    } catch (SocketException socketException) {
      //if a player leaves the game by closing the window, he gets replaced by an AI
      if (socketException.getMessage().equals("Connection reset") && controller.isGameStarted()) {
        controller.replacePlayerByAi(nickname);
      } else {
        socketException.printStackTrace();
      }
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    } finally {
      close();
    }
  }

  /**
   * Return the user-name of this specific client.
   *
   * @return The user-name as String.
   */
  public synchronized String getNickname() {
    return nickname;
  }

  /**
   * Set a name for this client.
   *
   * @param nickname The new name of this client.
   */
  public synchronized void setNickname(String nickname) {
    this.nickname = nickname;
  }

  /**
   * Check if the client has registered itself.
   *
   * @return <code>true</code> if the client is logged in, <code>false</code> otherwise
   */
  public boolean isLoggedIn() {
    return getNickname() != null;
  }

  /**
   * The handler has just received a new message from its client. This method processes them and
   * takes further steps depending on the message type.
   *
   * @param object A {@link JSONObject} containing the complete message sent from the client.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  private void handleMessage(JSONObject object) throws IOException {
    LOGGER.info(object);
    switch (JsonMessage.typeOf(object)) {
      case LOGIN -> handleLogin(object);
      case POST_MESSAGE -> handlePostMessage(object);
      case START_GAME -> handleStartGame();
      case NOTIFY_TILE_CHOSEN -> handleNotifyTileChosen(object);
      case PLACE_TILE_IN_PATTERN_LINE -> handlePlaceTileInPatternLine(object);
      case PLACE_TILE_IN_FLOOR_LINE -> handlePlaceTileInFloorLine();
      case REPLACE_PLAYER_BY_AI -> {
        controller.replacePlayerByAi(nickname);
      }
      case RESTART_GAME -> controller.restartGame();
      case CANCEL_GAME -> controller.cancelGameForAllPlayers();
      default -> throw new AssertionError("Unable to handle message " + object);
    }
  }

  /**
   * Process a login message and take further actions depending on the current state. If a user is
   * not already logged in and the username is still available, a broadcast is made that announces
   * the successful login of this user. Otherwise, the login fails and the client receives an
   * according attempt failed message.
   *
   * @param object A {@link JSONObject} containing a message with login-data.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  private void handleLogin(JSONObject object) throws IOException {
    try {
      if (controller.isGameStarted()) {
        send(JsonMessage.createGameNotStartableMessage(GameNotStartableEvent.GAME_ALREADY_STARTED));
        return;
      }

      if (nickname != null) {
        send(JsonMessage.loginFailed(LoginFailedEvent.ALREADY_LOGGED_IN));
        return;
      }

      if (nickname.length() > 15) {
        send(JsonMessage.loginFailed(LoginFailedEvent.NICKNAME_IS_TOO_LONG));
      }

      String nick = object.getString(JsonMessage.NICK_FIELD);
      if (!serverConnection.tryLogIn(nick)) {
        send(JsonMessage.loginFailed(LoginFailedEvent.NICKNAME_ALREADY_TAKEN));
        return;
      }

      setNickname(nick);
      controller.addPlayer(nick);
      send(JsonMessage.loginSuccess());

      JSONObject userJoinedMessage = JsonMessage.userJoined(nickname);
      serverConnection.broadcast(this, userJoinedMessage);
    } catch (JSONException jsonException) {
      throw new IllegalArgumentException("Failed to read a json object.", jsonException);
    }
  }

  /**
   * Start the game if enough player have logged in.
   */
  private void handleStartGame() throws IOException {
    if (GameModel.MIN_PLAYER_NUMBER > model.getPlayerNamesList().size()) {
      send(JsonMessage.createGameNotStartableMessage(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (controller.isGameStarted()) {
      send(JsonMessage.createGameNotStartableMessage(GameNotStartableEvent.GAME_ALREADY_STARTED));
    } else {
      controller.startGame();
    }
  }

  /**
   * Checks if it is this players turn (which allows him to make moves).
   *
   * @return <code>true</code> if its this players turn. <code>false</code> else.
   */
  private boolean isItThisPlayersTurn() {
    return model.getNickOfActivePlayer().equals(nickname);
  }

  /**
   * Process a post-message from the client which contains the information of a message that is to
   * be send to all the other connected clients.
   *
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  private void handlePostMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
      return;
    }
    String content = JsonMessage.getContent(object);
    if (content.equals("CYBERZUL HELP")) {
      String helpMessage = ChatMessageHandler.CYBERZUL_HELP;
      JSONObject cheatMessage = JsonMessage.createCheatMessage(helpMessage);
      send(cheatMessage);
    } else {
      JSONObject message = JsonMessage.message(getNickname(), new Date(), content);
      serverConnection.broadcast(this, message);
      System.out.println("is this entered?");
    }
  }

  /**
   * Closes the connection of this specific client handler.
   */
  public void close() {
    String nick = getNickname();
    try {
      if (nick != null) {
        serverConnection.broadcast(this, JsonMessage.userLeft(nick));
        setNickname(null);
      }
      serverConnection.removeHandlerFromList(this);
      if (!socket.isClosed()) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Send a message that goes to this specific client.
   *
   * @param message the message to be send as {@link JSONObject}
   * @throws IOException Thrown when writing to the output-stream fails.
   */
  public void send(JSONObject message) throws IOException {
    LOGGER.info("sent to client " + nickname + " " + message);
    String string = message + System.lineSeparator();
    if (socket.isClosed()) {
      return;
    }
    writer.write(string);
    writer.flush();
  }

  /**
   * Check if it's the players turn and if it is, notify the model about what Tile the player chose.
   * If it's not this players turn, inform him that he has to wait until he can make his next move.
   *
   * @param object the message that the client sent to the server.
   */
  private void handleNotifyTileChosen(JSONObject object) {
    try {
      if (isItThisPlayersTurn()) {
        int indexOfTile = object.getInt(JsonMessage.INDEX_OF_TILE_FIELD);
        int indexOfOffering = object.getInt(JsonMessage.INDEX_OF_OFFERING_FIELD);
        controller.chooseTileFrom(nickname, indexOfTile, indexOfOffering);
      } else {
        //notify the client that he has to wait and can't do his turn right now.
        send(JsonMessage.createMessageOfType(JsonMessage.NOT_YOUR_TURN));
      }
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if it's the players turn and if it is, notify the model that the tile should be placed at
   * the pattern line that is specified in the JSON Object that was sent from the client to the
   * server. If it's not this players turn, inform him that he has to wait until he can make his
   * next move.
   *
   * @param object the JSON Object that was sent from the client to the server.
   */
  private void handlePlaceTileInPatternLine(JSONObject object) {
    try {
      if (isItThisPlayersTurn()) {
        int indexOfPatternLine = object.getInt(JsonMessage.INDEX_OF_PATTERN_LINE_FIELD);
        controller.placeTileAtPatternLine(indexOfPatternLine);
      } else {
        //notify the client that he has to wait and can't do his turn right now.
        send(JsonMessage.createMessageOfType(JsonMessage.NOT_YOUR_TURN));
      }
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Check if it's the players turn. If yes, let the player place the tile in the floor line. If
   * it's not this players turn, inform him that he has to wait until the can make his next move.
   */
  private void handlePlaceTileInFloorLine() {
    try {
      if (isItThisPlayersTurn()) {
        controller.placeTileAtFloorLine();
      } else {
        //notify the client that he has to wait and can't do his turn right now.
        send(JsonMessage.createMessageOfType(JsonMessage.NOT_YOUR_TURN));
      }
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }
}
