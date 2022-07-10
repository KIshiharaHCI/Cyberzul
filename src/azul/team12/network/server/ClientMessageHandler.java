package azul.team12.network.server;

import static java.util.Objects.requireNonNull;

import azul.team12.controller.Controller;
import azul.team12.model.Model;
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


  /**
   * Set up a new {@link ClientMessageHandler} that deals with incoming and outgoing message for a
   * single connected client. The specific objects are created from the given socket.
   *
   * @param connection The connection handler that manages all the connected clients
   * @param socket The specific socket that belongs to this client
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
  public ClientMessageHandler(ServerNetworkConnection connection, Socket socket, Controller controller, Model model)
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
    switch (JsonMessage.typeOf(object)) {
      case LOGIN -> handleLogin(object);
      case POST_MESSAGE -> handlePostMessage(object);
      case START_GAME -> {
        //TODO: TEST SOUT
        System.out.println("Start Game in ClientMessageHandler");
        controller.startGame();}
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
    String string = message + System.lineSeparator();
    if (socket.isClosed()) {
      return;
    }
    writer.write(string);
    writer.flush();
  }
}
