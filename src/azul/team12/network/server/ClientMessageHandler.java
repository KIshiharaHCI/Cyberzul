package azul.team12.network.server;

import static java.util.Objects.requireNonNull;

import azul.team12.controller.Controller;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.shared.JsonMessage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
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

  private final ServerNetworkConnection serverConnection;

  private final Socket socket;

  private final BufferedReader reader;

  private final BufferedWriter writer;

  private String nickname;

  private Controller controller;

  private Model model;

  private static final Logger LOGGER = LogManager.getLogger(ClientMessageHandler.class);


  /**
   * Set up a new {@link ClientMessageHandler} that deals with incoming and outgoing message for a
   * single connected client. The specific objects are created from the given socket.
   *
   * @param connection The connection handler that manages all the connected clients
   * @param socket     The specific socket that belongs to this client
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
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
      case PLACE_TILE_IN_FLOOR_LINE -> handlePlaceTileInFloorLine(object);
      default -> throw new AssertionError("Unable to handle message " + object);
    }
  }

  /**
   * Process a login message and take further actions depending on the current state. If a
   * user is not already logged in and the username is still available, a broadcast is made
   * that announces the successful login of this user. Otherwise, the login fails and the client
   * receives an according attempt failed message.
   *
   * @param object A {@link JSONObject} containing a message with login-data.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  private void handleLogin(JSONObject object) throws IOException {
    if (nickname != null) {
      send(JsonMessage.loginFailed(LoginFailedEvent.ALREADY_LOGGED_IN));
      return;
    }

    String nick = JsonMessage.getNickname(object);
    if (!serverConnection.tryLogIn(nick)) {
      send(JsonMessage.loginFailed(LoginFailedEvent.NICKNAME_ALREADY_TAKEN));
      return;
    }

    setNickname(nick);
    controller.addPlayer(nick);
    send(JsonMessage.loginSuccess());
    serverConnection.broadcast(this, JsonMessage.userJoined(nick));
  }

  /**
   * Start the game if enough player have logged in.
   */
  private void handleStartGame() throws IOException {
    if (GameModel.MIN_PLAYER_NUMBER > model.getPlayerNamesList().size()) {
      send(JsonMessage.createGameNotStartableMessage(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else {
      controller.startGame();
    }
  }

  /**
   * Checks if it is this players turn (which allows him to make moves)
   *
   * @return <code>true</code> if its this players turn. <code>false</code> else.
   */
  private boolean isItThisPlayersTurn() {
    if (model.getNickOfActivePlayer().equals(nickname)) {
      return true;
    }
    return false;
  }

  /**
   * Process a post-message from the client which contains the information of a message that is
   * to be send to all the other connected clients.
   *
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  private void handlePostMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
      // A user needs to log in first before being able to send messages.
      return;
    }

    String content = JsonMessage.getContent(object);
    JSONObject message = JsonMessage.message(getNickname(), new Date(), content);
    serverConnection.broadcast(this, message);
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
      serverConnection.handlerClosed(this);
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
        model.notifyTileChosen(nickname, indexOfTile, indexOfOffering);
      } else {
        //notify the client that he has to wait and can't do his turn right now.
        send(JsonMessage.createMessageOfType(JsonMessage.NOT_YOUR_TURN));
      }
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if it's the players turn and if it is, notify the model that the tile should be placed
   * at the pattern line that is specified in the JSON Object that was sent from the client to the
   * server.
   * If it's not this players turn, inform him that he has to wait until he can make his next move.
   *
   * @param object the JSON Object that was sent from the client to the server.
   */
  private void handlePlaceTileInPatternLine(JSONObject object) {
    try {
      if (isItThisPlayersTurn()) {
        int indexOfPatternLine = object.getInt(JsonMessage.INDEX_OF_PATTERN_LINE_FIELD);
        model.makeActivePlayerPlaceTile(indexOfPatternLine);
      } else {
        //notify the client that he has to wait and can't do his turn right now.
        send(JsonMessage.createMessageOfType(JsonMessage.NOT_YOUR_TURN));
      }
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Check if it's the players turn. If yes, let the player place the tile in the floor line.
   * If it's not this players turn, inform him that he has to wait until the can make his next move.
   *
   * @param object
   */
  private void handlePlaceTileInFloorLine(JSONObject object) {
    try {
      if (isItThisPlayersTurn()) {
        model.tileFallsDown();
      } else {
        //notify the client that he has to wait and can't do his turn right now.
        send(JsonMessage.createMessageOfType(JsonMessage.NOT_YOUR_TURN));
      }
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }
}
