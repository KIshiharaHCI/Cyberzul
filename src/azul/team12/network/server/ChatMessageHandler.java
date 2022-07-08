package azul.team12.network.server;

import azul.team12.shared.JsonMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static java.util.Objects.requireNonNull;

/**
 * Handles the chat messages that are sent from the clients to the server.
 */
public class ChatMessageHandler {
  private final ServerNetworkConnection serverConnection;

  private final Socket socket;

  private final BufferedReader reader;

  private final BufferedWriter writer;

  private String nickname;

  /**
   * Create a new {@link ChatMessageHandler} that deals with incoming and outgoing message for a
   * single connected client. The specific objects are created from the given socket.
   *
   * @param connection The connection handler that manages all the connected clients
   * @param socket The specific socket that belongs to this client
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
  public ChatMessageHandler(ServerNetworkConnection connection, Socket socket)
          throws IOException {
    serverConnection = requireNonNull(connection);
    this.socket = requireNonNull(socket);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }


  /**
   * Return the user-name of the message sender
   *
   * @return The user-name of sender.
   */
  public synchronized String getNickname() {
        return nickname;
    }

  /**
   * Set a name for this message sender.
   *
   * @param nickname The new name of message sender.
   */
  public synchronized void setNickname(String nickname) {
        this.nickname = nickname;
    }



  /**
   * Process a post-message from the client which contains the information of a message that is
   * to be send to all the other connected clients.
   *
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  public void handlePostMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
        // A user needs to log in first before being able to send messages.
        return;
    }

    String content = JsonMessage.getContent(object);
    JSONObject message = JsonMessage.message(getNickname(), new Date(), content);
    /*for (Player player : playerList) {
      player.getWriter().write(message + System.lineSeparator());
      Player.getWriter().flush();*/
    }


}

