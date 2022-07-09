package azul.team12.network.server;

import azul.team12.shared.JsonMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Date;

import static java.util.Objects.requireNonNull;

/**
 * Handles the chat messages that are sent from the clients to the server.
 */
public class ChatMessageHandler extends ClientMessageHandler {

  private String nickname;
  private ServerNetworkConnection serverConnection;

  /**
   * Create a new {@link ChatMessageHandler} that deals with incoming and outgoing message for a
   * single connected player. The specific objects are created from the given socket.
   *
   * @param serverConnection The connection handler that manages all the connected players
   * @param socket The specific socket that belongs to this player
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
  public ChatMessageHandler(ServerNetworkConnection serverConnection, Socket socket)
          throws IOException {
    super(serverConnection, socket);

    }


  /**
   * Return the player-name of the message sender
   *
   * @return The player-name of sender.
   */
  public synchronized String getNickname() {
        return nickname;
    }



  /**
   * Process a post-message from the client which contains the information of a message that is
   * to be send to all the other connected players.
   *
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  public void handlePostMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
      System.out.println("Please longin before sending messages.");
    }

    String content = JsonMessage.getContent(object);
    JSONObject message = JsonMessage.message(getNickname(), new Date(), content);
    serverConnection.broadcast(this, message);
  }

  public void handleCheatMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
      System.out.println("Please longin before sending messages.");
    }

    String content = JsonMessage.getContent(object);
    //JSONObject cheatMessage = JsonMessage.message();
    //serverConnection.broadcast(this, cheatMessage);

  }
}

