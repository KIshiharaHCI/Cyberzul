package cyberzul.network.client;

import cyberzul.network.client.messages.PlayerTextMessage;
import cyberzul.network.server.Server;
import cyberzul.network.shared.JsonMessage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The network-connection of the client. Establishes a connection to the server and takes care of
 * sending and receiving messages in JSON format.
 */
public class ClientNetworkConnection {

  private static byte[] HOST;
  private static final int PORT = 8080;

  private final ClientModel model;
  private Socket socket;
  private BufferedWriter writer;
  private BufferedReader reader;
  private Thread thread;


  //this class needs this reference to this mutable objects.
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public ClientNetworkConnection(ClientModel model, byte[] host) {
    this.model = model;
    HOST = host;
  }

  /**
   * Start the network connection.
   */
  public synchronized void start() {
    thread = new Thread(this::doConnectLoop);
    thread.setDaemon(true);
    thread.start();
  }

  private void doConnectLoop() {
    try {
      while (!Thread.interrupted()) {
        Socket socket;
        try {
          socket = new Socket(InetAddress.getByAddress(HOST), PORT);
        } catch (ConnectException connectException) {
          if (connectException.getMessage().equals("Connection refused: connect")) {
            Server.start();
            continue;
          } else {
            connectException.printStackTrace();
            break;
          }
        } catch (UnknownHostException e) {
          e.printStackTrace();
          break;
        } catch (IOException e) {
          e.printStackTrace();
          Thread.sleep(1000);
          continue;
        }
        try {
          setupConnection(socket);
        } catch (IOException e) {
          e.printStackTrace();
          break;
        }
        doInputLoop();
      }
    } catch (InterruptedException ex) {
      // Acknowledged.
      // (Socket was requested to shut down, e.g. by the user pressing ctrl+d)
    }
  }

  private synchronized void setupConnection(Socket socket) throws IOException {
    this.socket = socket;
    writer = new BufferedWriter(
        new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
    reader =
        new BufferedReader(
            new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));
  }

  private void doInputLoop() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
          break;
        }

        JSONObject object = new JSONObject(line);
        handleMessage(object);
      } catch (IOException e) {
        closeSocket();
        break;
      } catch (JSONException e) {
        e.printStackTrace();
        closeSocket();
        break;
      }
    }
    System.out.println("Input loop ended.");
  }

  /**
   * Check the type of the message that was sent from the server to this client and distribute it
   * to the correct handling method.
   *
   * @param object the message that was sent from the server to the client.
   * @throws JSONException is thrown if the JSONObject was defective.
   */
  public void handleMessage(JSONObject object) throws JSONException {
    switch (JsonMessage.typeOf(object)) {
      case CONNECTED -> model.connected(object.getJSONArray(JsonMessage.PLAYER_NAMES_FIELD));
      case LOGIN_SUCCESS -> model.loggedIn();
      case LOGIN_FAILED -> model.loginFailed(object.getString(JsonMessage.ADDITIONAL_INFORMATION));
      case GAME_STARTED -> handleGameStarted(object);
      case USER_JOINED -> model.userJoined(object.getString(JsonMessage.NICK_FIELD));
      case USER_LEFT -> {
        //TODO: IMPLEMENT CHAT HERE @XUE
      }
      case NEXT_PLAYERS_TURN -> model.handleNextPlayersTurn(object);
      case NOT_YOUR_TURN -> model.handleNotYourTurn();
      case PLAYER_HAS_CHOSEN_TILE -> model.handlePlayerHasChosenTile(object);
      case NO_VALID_TURN_TO_MAKE -> model.handleNoValidTurnToMake();
      case GAME_NOT_STARTABLE -> model.handleGameNotStartable(
          object.getString(JsonMessage.ADDITIONAL_INFORMATION));
      case ILLEGAL_TURN -> model.handleIllegalTurn();
      case ROUND_FINISHED -> model.handleRoundFinished(object);
      case GAME_FINISHED -> model.handleGameFinishedEvent(object);
      case GAME_CANCELED -> model.handleGameCanceled(object.getString(JsonMessage.NICK_FIELD));
      case GAME_FORFEITED -> model.handleGameForfeited(object.getString(JsonMessage.NICK_FIELD));
      default -> throw new AssertionError("Unhandled message: " + object);
    }
  }

  private void handleGameStarted(JSONObject object) throws JSONException {
    JSONArray offerings = object.getJSONArray(JsonMessage.OFFERINGS_FIELD);
    JSONArray playerNames = object.getJSONArray(JsonMessage.PLAYER_NAMES_FIELD);
    model.handleGameStarted(offerings, playerNames);
  }

  /**
   * Stop the network-connection.
   */
  public void stop() {
    synchronized (this) {
      thread.interrupt();
    }
    closeSocket();
  }

  private synchronized void closeSocket() {
    if (socket != null) {
      try {
        socket.close();
      } catch (IOException e) {
        // We want the socket to shut down anyway, so we do not care about an exception here
        e.printStackTrace();
      }
    }
    reader = null;
    writer = null;
    socket = null;
  }

  /**
   * Send a login-request to the server.
   *
   * @param nickname The name of the user with whom to log in.
   */
  public void sendLogin(String nickname) {
    JSONObject login = JsonMessage.login(nickname);
    send(login);
  }

  /**
   * Send a chat message to the server.
   *
   * @param chatMessage The {@link PlayerTextMessage} containing the message of the user.
   */

  public void playerSendMessage(PlayerTextMessage chatMessage) {
    JSONObject message = JsonMessage.postMessage(chatMessage.getContent());
    send(message);
  }

  synchronized void send(JSONObject message) {
    try {
      writer.write(message + System.lineSeparator());
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  synchronized void send(JsonMessage messageType) {
    try {
      JSONObject message = JsonMessage.createMessageOfType(messageType);
      writer.write(message + System.lineSeparator());
      writer.flush();
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
  }
}

