package azul.team12.network.server;

import azul.team12.controller.Controller;
import azul.team12.model.Model;
import azul.team12.shared.JsonMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.Date;

import static java.util.Objects.requireNonNull;

/**
 * Handles the chat messages that are sent from the players to the server.
 */
public class ChatMessageHandler extends ClientMessageHandler {

  private static String nickname;
  private static ServerNetworkConnection serverConnection;
  private Controller controller;
  private Model model;
  private static final String AZUL_STRATEGY = """
          Azul - Strategy:
          Focus on Negative Points.    Azul is point based, which causes us to focus on the amount of points we are gaining.
          Forget About 5 of a Kind.    There is something inherently wrong with trying to place all 5 of the same color tile on your board: 
                                       it’s a terribly inefficient way to get points.
          Start in the Center.         There’s a very simple reason for choosing the blue tile right in the middle of the board as the first placement: 
                                       it gives you a ton of options to build on for the rest of the game.
          Play Defensive.              Blocking your opponents from moves that would net them a ton of points is an 
                                       absolutely devastating tactic in 2 player games and very useful with more players too.
          The Quick Game.              As with a lot of board games, focusing on the end conditions is important. 
                                       In this case, filling up a row with tiles stops the game and determines the winner.""";

  /**
   * Create a new {@link ChatMessageHandler} that deals with incoming and outgoing message for a
   * single connected player. The specific objects are created from the given socket.
   *
   * @param serverConnection The connection handler that manages all the connected players
   * @param socket The specific socket that belongs to this player
   * @throws IOException Thrown when failing to retrieve the In- or Output-stream.
   */
  public ChatMessageHandler(ServerNetworkConnection serverConnection, Socket socket, Controller controller, Model model)
          throws IOException {
    super(serverConnection, socket, controller, model);

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
   * Process a post-message from the player which contains the information of a message
   * that is to be sent to all the other connected players.
   *
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  public void handlePostMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
      System.out.println("Please login before sending messages.");
    }

    String content = JsonMessage.getContent(object);
    JSONObject message = JsonMessage.message(getNickname(), new Date(), content);
    serverConnection.broadcast(this, message);
  }

  /**
   * Process a help-message and sent this to the player who requires.
   * @param object A {@link JSONObject} containing the data for a post-message.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  public void handleCheatMessage(JSONObject object) throws IOException {
    if (nickname == null || nickname.isBlank()) {
      System.out.println("Please login before sending messages.");
    }

    JSONObject cheatMessage = JsonMessage.createCheatMessage(nickname, AZUL_STRATEGY);
    serverConnection.broadcast(this, cheatMessage);

  }
}

