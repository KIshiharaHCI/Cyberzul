package cyberzul.network.server;

import cyberzul.controller.Controller;
import cyberzul.model.Model;
import cyberzul.shared.JsonMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


/**
 * Handles the chat messages that are send from the clients to the server.
 * */

public class ChatMessageHandler extends ClientMessageHandler implements Runnable {

  private static String nickname;

  //TODO: make this final
  private Socket socket;

  private final BufferedReader reader;

  private final BufferedWriter writer;


  private static ServerNetworkConnection serverConnection;



  /*public static final String[] CYBERZUL_HELP = {
    "Azul - Strategy:"
    //"Focus on Negative Points. Azul is point based, which causes us to focus on the amount of points we are gaining.",
    //"Forget About 5 of a Kind. There is something inherently wrong with trying to place all 5 of the same color tile on your board.",
    //"Start in the Center. There’s a very simple reason for choosing the blue tile right in the middle of the board as the first placement.",
    //"Play Defensive. Blocking your opponents from moves that would net them a ton of points is an absolutely devastating tactic in 2 player games and very useful with more players too.",
    //"The Quick Game. As with a lot of board games, focusing on the end conditions is important."
  };*/

  public static final String CYBERZUL_HELP = """
  Azul - Strategy:
  Azul is point based, which causes us to focus 
  on the amount of points we are gaining. There 
  is something inherently wrong with trying to 
  place all 5 of the same color tile on your board:
  it’s a terribly inefficient way to get points.
  There’s a very simple reason for choosing the blue
  tile right in the middle of the board as the first placement:
  it gives you a ton of options to build on for the rest of the 
  game. Blocking your opponents from moves that would net them 
  a ton of points is an absolutely devastating tactic in 
  2 player games and very useful with more players too. 
  As with a lot of board games, focusing on the end conditions
  is important. In this case, filling up a row with tiles stops 
  the game and determines the winner.""";

  /**
   * Create a new {@link ChatMessageHandler} that deals with incoming and outgoing message for a
   * single connected player. The specific objects are created from the given socket.
   *
   * @param serverConnection The connection handler that manages all the connected players
   * @param socket The specific socket that belongs to this player
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
  public ChatMessageHandler(
      ServerNetworkConnection serverConnection, Socket socket, Controller controller, Model model)
      throws IOException {
    super(serverConnection, socket, controller, model);
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
                handleChatMessage(object);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

  /**
   * Return the player-name of the message sender
   *
   * @return The player-name of sender.
   */
  /*public synchronized String getNickname() {
    return nickname;
  }

  @Override
  public String toString() {

    StringBuilder newString = new StringBuilder();

    for (String row : AZUL_STRATEGY) {
      String[] details = row.split(",");
      for (String detail : details) {
        newString.append(detail + " ");
      }
      newString.deleteCharAt(newString.length() - 1);
      newString.append("\n");
    }
    return newString.toString();
  }*/

  /**
   * The handler has just received a new message from its client. This method processes them and
   * takes further steps depending on the message type.
   *
   * @param object A {@link JSONObject} containing the complete message sent from the client.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */

  private void handleChatMessage(JSONObject object) throws IOException {
      switch (JsonMessage.typeOf(object)) {
          case CHEAT_MESSAGE -> handleCheatMessage(object);
          default -> throw new AssertionError("Unable to handle message " + object);
      }
  }

  /**
   * Process a help-message and sent this to the player who requires.
   *
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  private void handleCheatMessage(JSONObject object) throws IOException {
      if (nickname == null || nickname.isBlank()) {
          System.out.println("Please login before sending messages.");
      }
      String content = JsonMessage.getContent(object);
      if (content.equals("CYBERZUL HELP")){
          JSONObject cheatMessage = JsonMessage.createCheatMessage(toString());
          serverConnection.broadcast(this, cheatMessage);
      }

  }

}
