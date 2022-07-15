package azul.team12.network.server;

import azul.team12.model.Model;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.Player;
import azul.team12.model.events.GameCanceledEvent;
import azul.team12.model.events.GameFinishedEvent;
import azul.team12.model.events.GameForfeitedEvent;
import azul.team12.model.events.NextPlayersTurnEvent;
import azul.team12.model.events.PlayerHasChosenTileEvent;
import azul.team12.shared.JsonMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handles the events that are fired from the GameModel to the Server.
 * <p>
 * Some messages are only broadcasted to the active player while others are broadcasted to
 * everyone.
 */
public class ModelPropertyChangeHandler implements PropertyChangeListener {

  private ServerNetworkConnection connection;
  private Model model;

  public ModelPropertyChangeHandler(ServerNetworkConnection connection, Model model) {

    //TODO: TEST
    System.out.println(model + " " + connection);

    this.connection = connection;
    this.model = model;
    model.addPropertyChangeListener(this);
  }


  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(() -> handleModelUpdate(event));
  }

  /**
   * Handles the events that get fired from the model to the listeners of the model.
   *
   * @param event the event that was fired from the model.
   */
  private void handleModelUpdate(PropertyChangeEvent event) {
    Object customMadeGameEvent = event.getNewValue();

    String eventName = event.getPropertyName();

    switch (eventName) {
      case "GameStartedEvent" -> handleGameStartedEvent();
      case "NextPlayersTurnEvent" -> handleNextPlayersTurnEvent(customMadeGameEvent);
      case "LoggedInEvent" -> {
        //TODO: FILL THIS WITH FUNCTIONALITY
      }
      case "PlayerHasChosenTileEvent" -> handlePlayerHasChosenTileEvent(customMadeGameEvent);
      case "NoValidTurnToMakeEvent" -> handleNoValidTurnToMakeEvent();
      case "IllegalTurnEvent" -> handleIllegalTurnEvent();
      case "RoundFinishedEvent" -> handleRoundFinishedEvent();
      case GameFinishedEvent.EVENT_NAME -> handleGameFinishedEvent(customMadeGameEvent);
      case GameCanceledEvent.EVENT_NAME -> handleGameCanceledEvent(customMadeGameEvent);
      case GameForfeitedEvent.EVENT_NAME -> handleGameForfeitedEvent(customMadeGameEvent);
      default -> throw new AssertionError("Unknown event: " + eventName);
    }
  }

  /**
   * If the player successfully chose a tile, this method gets executed.
   *
   * @param customMadeGameEvent the event that is fired if the player chose his tile in the
   *                            GameModel.
   */
  private void handlePlayerHasChosenTileEvent(Object customMadeGameEvent) {
    try {
      PlayerHasChosenTileEvent playerHasChosenTileEvent =
          (PlayerHasChosenTileEvent) customMadeGameEvent;
      connection.broadcastToAll(
          JsonMessage.createPlayerHasChosenTileMessage(playerHasChosenTileEvent.getNickname()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleGameFinishedEvent(Object customMadeGameEvent) {
    GameFinishedEvent gameFinishedEvent = (GameFinishedEvent) customMadeGameEvent;
    String winner = gameFinishedEvent.getWinner();
    try {
      JSONObject message = JsonMessage.createMessageOfType(JsonMessage.GAME_FINISHED);
      message.put(JsonMessage.PLAYER_FIELD, completePlayerUpdateMessage());
      message.put(JsonMessage.NICK_FIELD, winner);
      connection.broadcastToAll(message);
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }

  private void handleRoundFinishedEvent() {
    try {
      JSONObject message = JsonMessage.createMessageOfType(JsonMessage.ROUND_FINISHED);
      message.put(JsonMessage.PLAYER_FIELD, completePlayerUpdateMessage());
      message.put(JsonMessage.INDEX_OF_PLAYER_WITH_SPM, model.getIndexOfPlayerWithSpm());
      connection.broadcastToAll(message);
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
  }

  private JSONArray completePlayerUpdateMessage() throws JSONException {
    JSONArray playerArray = new JSONArray();
    for (String playerName : model.getPlayerNamesList()) {
      JSONObject playerObject = new JSONObject();
      Player player = model.getPlayerByName(playerName);
      playerObject.put(JsonMessage.NICK_FIELD, playerName);
      playerObject.put(JsonMessage.POINTS_FIELD, player.getPoints());
      playerObject.put(JsonMessage.PATTERN_LINES_FIELD,
          JsonMessage.parsePatternLinesToJSONArray(player.getPatternLines()));
      playerObject.put(JsonMessage.FLOOR_LINE_FIELD,
          JsonMessage.parseFloorLineToJSONArray(player.getFloorLine()));
      playerObject.put(JsonMessage.WALL_FIELD,
          JsonMessage.parsePatternLinesToJSONArray(model.getWallOfPlayer(playerName)));
      playerArray.put(playerObject);
    }
    return playerArray;
  }

  private void handleIllegalTurnEvent() {
    try {
      connection.sendToActivePlayer(JsonMessage.createMessageOfType(JsonMessage.ILLEGAL_TURN));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void handleNoValidTurnToMakeEvent() {
    try {
      connection.sendToActivePlayer(
          JsonMessage.createMessageOfType(JsonMessage.NO_VALID_TURN_TO_MAKE));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /**
   * If the model informs this listener that the game started, this listener fetches the content of
   * the offerings and the player names and broadcasts them to all clients.
   */
  private void handleGameStartedEvent() {
    try {
      List<Offering> offerings = model.getOfferings();
      List<String> playerNames = model.getPlayerNamesList();
      connection.broadcastToAll(JsonMessage.createGameStartedMessage(offerings, playerNames));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleNextPlayersTurnEvent(Object customMadeGameEvent) {
    try {
      NextPlayersTurnEvent nextPlayersTurnEvent = (NextPlayersTurnEvent) customMadeGameEvent;
      String nameOfActivePlayer = nextPlayersTurnEvent.getNameOfActivePlayer();

      List<Offering> offerings = model.getOfferings();

      String nameOfPlayerWhoEndedHisTurn = nextPlayersTurnEvent.getNameOfPlayerWhoEndedHisTurn();
      ModelTile[][] newPatternLinesOfPlayerWhoEndedHisTurn =
          model.getPatternLinesOfPlayer(nameOfPlayerWhoEndedHisTurn);
      List<ModelTile> newFloorLineOfPlayerWhoEndedHisTurn =
          model.getFloorLineOfPlayer(nameOfPlayerWhoEndedHisTurn);

      connection.broadcastToAll(
          JsonMessage.createNextPlayersTurnMessage(nameOfActivePlayer, offerings,
              nameOfPlayerWhoEndedHisTurn,
              newPatternLinesOfPlayerWhoEndedHisTurn, newFloorLineOfPlayerWhoEndedHisTurn));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleGameCanceledEvent(Object customMadeGameEvent) {
    try {
      GameCanceledEvent gameCanceledEvent = (GameCanceledEvent) customMadeGameEvent;
      String nameOfCanceler = gameCanceledEvent.getNameOfPersonThatCanceled();

      JSONObject message = JsonMessage.createMessageOfType(JsonMessage.GAME_CANCELED);
      message.put(JsonMessage.NICK_FIELD, nameOfCanceler);

      connection.broadcastToAll(message);
    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }

  private void handleGameForfeitedEvent(Object customMadeGameEvent) {
    try {
      GameForfeitedEvent gameForfeitedEvent = (GameForfeitedEvent) customMadeGameEvent;

      String nameOfThePlayerWhoForfeited = gameForfeitedEvent.getForfeiter();

      JSONObject message = JsonMessage.createMessageOfType(JsonMessage.GAME_FORFEITED);
      message.put(JsonMessage.NICK_FIELD, nameOfThePlayerWhoForfeited);

      connection.handlerClosed(nameOfThePlayerWhoForfeited);
      connection.broadcastToAll(message);

    } catch (JSONException | IOException e) {
      e.printStackTrace();
    }
  }
}
