package cyberzul.network.shared;

import cyberzul.model.ModelTile;
import cyberzul.model.Offering;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The messages that are sent back and forth between the server and the clients.
 */
public enum JsonMessage {
  // message types that are linked to the chat messages and the connection
  LOGIN("login"),
  LOGIN_SUCCESS("login success"),
  LOGIN_FAILED("login failed"),
  USER_JOINED("user joined"),
  POST_MESSAGE("post message"),
  MESSAGE("message"),
  USER_LEFT("user left"),
  CHEAT_MESSAGE("cheat message"),

  // messages from the client to the server
  END_TURN("end turn"),
  FORFEIT_GAME("forfeit game"),
  START_GAME("start game"),
  RESTART_GAME("restart game"),
  CANCEL_GAME("cancel game"),
  NOTIFY_TILE_CHOSEN("notify tile chosen"),
  PLACE_TILE_IN_PATTERN_LINE("place tile in pattern line"),
  PLACE_TILE_IN_FLOOR_LINE("place tile in floor line"),
  REPLACE_PLAYER_BY_AI("replace player by ai"),

  // messages from the server to the client
  CONNECTED("connected"),
  GAME_STARTED("game started"),
  NOT_YOUR_TURN("not your turn"),
  NEXT_PLAYERS_TURN("next players turn"),
  PLAYER_HAS_CHOSEN_TILE("player has chosen tile"),
  NO_VALID_TURN_TO_MAKE("no valid turn to make"),
  ILLEGAL_TURN("illegal turn"),
  GAME_NOT_STARTABLE("game not startable"),
  ROUND_FINISHED("round finished"),
  GAME_FINISHED("game finished"),
  GAME_FORFEITED("game forfeited"),
  GAME_CANCELED("game canceled");

  public static final String TYPE_FIELD = "type";

  public static final String NICK_FIELD = "nick";

  public static final String CONTENT_FIELD = "content";

  public static final String TIME_FIELD = "time";

  public static final String ADDITIONAL_INFORMATION = "additional information";

  public static final String OFFERINGS_FIELD = "offerings";

  public static final String PLAYER_NAMES_FIELD = "player names";

  public static final String INDEX_OF_TILE_FIELD = "index of tile ";

  public static final String INDEX_OF_OFFERING_FIELD = "index of offering";

  public static final String INDEX_OF_ACTIVE_PLAYER_FIELD = "index of active player";

  public static final String INDEX_OF_PATTERN_LINE_FIELD = "index of pattern line";

  public static final String PATTERN_LINES_FIELD = "pattern lines";

  public static final String FLOOR_LINE_FIELD = "floor line";

  public static final String INDEX_OF_PLAYER_WITH_SPM =
      "index of player with starting player marker";

  public static final String NAME_OF_ACTIVE_PLAYER_FIELD = "name of active player";

  public static final String NAME_OF_PLAYER_WHO_ENDED_HIS_TURN_FIELD =
      "name of player who ended his turn";

  public static final String POINTS_FIELD = "points";

  public static final String WALL_FIELD = "wall";

  public static final String PLAYER_FIELD = "player";

  private final String jsonName;

  /**
   * Create an instance of a JsonMessage.
   *
   * @param jsonName the String representation of the JsonMessage.
   */
  JsonMessage(String jsonName) {
    this.jsonName = jsonName;
  }

  /**
   * If the JSONObject has a type that is element of this enum, return this type. Else throw
   * an IllegalArgumentException.
   *
   * @param message the message whose type we want to find out.
   * @return the type of this message.
   * @throws IllegalArgumentException if <cod>message</cod> has a type that is not element of this
   *                                  enum.
   */
  public static JsonMessage typeOf(JSONObject message) {
    String typeName;
    try {
      typeName = message.getString(TYPE_FIELD);
    } catch (JSONException e) {
      throw new IllegalArgumentException(String.format("Unknown message type '%s'", message), e);
    }

    Optional<JsonMessage> opt =
        Arrays.stream(JsonMessage.values())
            .filter(x -> x.getJsonName().equals(typeName))
            .findFirst();
    return opt.orElseThrow(
        () -> new IllegalArgumentException(String.format("Unknown message type '%s'", typeName)));
  }

  // Controller methods

  /**
   * Create a JSONMessage that tells the server which tile the player choose and from which
   * offering.
   *
   * @param indexOfTile     the index of the tile in the Offering.
   * @param indexOfOffering <code>0</code> means the TableCenter. Every following index is a
   *                        FactoryDisplay.
   * @return A JSONMessage telling the server that a tile was chosen by the player, and which one.
   */
  public static JSONObject notifyTileChosenMessage(int indexOfTile, int indexOfOffering) {
    try {
      JSONObject jsonObject = createMessageOfType(NOTIFY_TILE_CHOSEN);
      jsonObject.put(INDEX_OF_TILE_FIELD, indexOfTile);
      jsonObject.put(INDEX_OF_OFFERING_FIELD, indexOfOffering);
      return jsonObject;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Provides the ClientModels with all information that is needed to start the game.
   *
   * @param offerings   contains the TableCenter and the FactoryDisplays.
   * @param playerNames contains the names of the player.
   * @return a message that can be send to all clients, informing them that the game started.
   */
  public static JSONObject createGameStartedMessage(
      List<Offering> offerings, List<String> playerNames) {
    try {
      JSONObject returnObject = createMessageOfType(GAME_STARTED);
      returnObject.put(OFFERINGS_FIELD, parseOfferingsToJsonArray(offerings));
      returnObject.put(PLAYER_NAMES_FIELD, parsePlayerNamesToJsonArray(playerNames));

      return returnObject;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a JSONObject that informs that the game can't be started.
   *
   * @param reason The reason why this game could not be started.
   * @return a String in JSON format that informs that the game can't be started and why.
   */
  public static JSONObject createGameNotStartableMessage(String reason) {
    try {
      JSONObject returnObject = createMessageOfType(GAME_NOT_STARTABLE);
      returnObject.put(ADDITIONAL_INFORMATION, reason);
      return returnObject;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * This message gets created by the server to inform the clients that the active player has chosen
   * a tile.
   *
   * @return a JSONObject that can be distributed via output streams.
   */
  public static JSONObject createPlayerHasChosenTileMessage(String nameOfActivePlayer) {
    try {
      JSONObject message = createMessageOfType(PLAYER_HAS_CHOSEN_TILE);
      message.put(NAME_OF_ACTIVE_PLAYER_FIELD, nameOfActivePlayer);
      return message;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * This method creates a JSONArray containing all Offerings. The first Offering is the Table
   * Center.
   *
   * @param offerings a list of all offerings in the game.
   * @return a two dimensional array containing the contents of all offerings.
   */
  public static JSONArray parseOfferingsToJsonArray(List<Offering> offerings) {
    JSONArray offeringsArray = new JSONArray();
    for (Offering o : offerings) {
      JSONArray currentOffering = new JSONArray();
      for (ModelTile tile : o.getContent()) {
        currentOffering.put(tile.toString());
      }
      offeringsArray.put(currentOffering);
    }
    return offeringsArray;
  }

  /**
   * Take a list of player names as Strings and parse them into a JSONArray, so it can be sent via
   * OutputStream.
   * The most prominent use case is the list of players that are currently logged in.
   *
   * @param playerNames a list of player names as Strings.
   * @return a list of player names as JSON array.
   */
  public static JSONArray parsePlayerNamesToJsonArray(List<String> playerNames) {
    JSONArray playerNamesArray = new JSONArray();
    for (String nick : playerNames) {
      playerNamesArray.put(nick);
    }
    return playerNamesArray;
  }

  // TODO: WRITE THIS METHOD

  /**
   * This method is created by the server to notify the players that a player has done his turn and
   * a new player has to do his turn now. It contains all information about what changes occurred
   * during the past turn.
   *
   * @param offerings                              the up-to-date information about the content of
   *                                               all FactoryDisplays and the TableCenter.
   * @param nameOfPlayerWhoEndedHisTurn            the name of the player who just ended his turn.
   * @param newPatternLinesOfPlayerWhoEndedHisTurn the up-to-date pattern lines of the player who
   *                                               just ended his turn.
   * @param newFloorLineOfPlayerWhoEndedHisTurn    the up-to-date floor line of the player who just
   *                                               ended his turn.
   * @return a String in JSON format that contains all the information that is needed to update the
   *         data of the clients after a player ended his turn.
   */
  public static JSONObject createNextPlayersTurnMessage(
      String nameOfActivePlayer,
      List<Offering> offerings,
      String nameOfPlayerWhoEndedHisTurn,
      ModelTile[][] newPatternLinesOfPlayerWhoEndedHisTurn,
      List<ModelTile> newFloorLineOfPlayerWhoEndedHisTurn) {
    try {
      JSONObject returnObject = createMessageOfType(NEXT_PLAYERS_TURN);

      returnObject.put(NAME_OF_ACTIVE_PLAYER_FIELD, nameOfActivePlayer);

      returnObject.put(OFFERINGS_FIELD, parseOfferingsToJsonArray(offerings));

      returnObject.put(NAME_OF_PLAYER_WHO_ENDED_HIS_TURN_FIELD, nameOfPlayerWhoEndedHisTurn);

      returnObject.put(
          PATTERN_LINES_FIELD,
          parsePatternLinesToJsonArray(newPatternLinesOfPlayerWhoEndedHisTurn));

      returnObject.put(
          FLOOR_LINE_FIELD, parseFloorLineToJsonArray(newFloorLineOfPlayerWhoEndedHisTurn));

      return returnObject;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Write the pattern lines of one player into a JSONArray.
   *
   * @param patternLines the pattern lines of a single player.
   * @return a JSONArray containing the ModelTiles of his pattern lines.
   */
  public static JSONArray parsePatternLinesToJsonArray(ModelTile[][] patternLines) {
    JSONArray patternLinesArray = new JSONArray();
    for (int row = 0; row < patternLines.length; row++) {
      JSONArray line = new JSONArray();
      for (int col = 0; col < patternLines[row].length; col++) {
        line.put(patternLines[row][col]);
      }
      patternLinesArray.put(line);
    }
    return patternLinesArray;
  }

  /**
   * Write the floor line of one player into a JSONArray.
   *
   * @param floorLine the floor line of a single player.
   * @return a JSONArray containing the ModelTiles of that floor line.
   */
  public static JSONArray parseFloorLineToJsonArray(List<ModelTile> floorLine) {
    JSONArray floorLineArray = new JSONArray();
    for (ModelTile t : floorLine) {
      floorLineArray.put(t);
    }
    return floorLineArray;
  }

  /**
   * Create a new JSONObject with the specified type.
   *
   * @param type the type of this JSONObject
   * @return a JSONObject that has the specified type.
   * @throws JSONException if for example the specified type is not element of this enum.
   */
  public static JSONObject createMessageOfType(JsonMessage type) throws JSONException {
    return new JSONObject().put(TYPE_FIELD, type.getJsonName());
  }

  /**
   * Create a JSONObject by passing only the name of the method and the nick of the player. I.e.
   * "createJSONMessage(GET_POINTS,jonas123);
   *
   * @param methodName the name of the method in the Controller Interface.
   * @param nickname   the nickname of the player that is passed to the method.
   * @return a JSONObject containing the information that this specific method was invoked with the
   *         name of the player with whom the method was invoked.
   */
  public static JSONObject createMessageOfType(JsonMessage methodName, String nickname) {
    try {
      return createMessageOfType(methodName).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a JSONObject with type JsonMessage.MESSAGE and information about the sender, the time
   * and what the sender wrote.
   *
   * @param nickname the name of the sender.
   * @param time     the time that this object was created.
   * @param content  the message that the sender wrote.
   * @return a String in JSON format containing the information mentioned above.
   */
  public static JSONObject message(String nickname, Date time, String content) {
    try {
      JSONObject message = createMessageOfType(MESSAGE);
      message.put(CONTENT_FIELD, content);
      message.put(NICK_FIELD, nickname);
      message.put(TIME_FIELD, convertDateToString(time));

      return message;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message in JSON format that is sent from the client to the server, containing the
   * information that the client requests to log in with the conveyed nickname.
   *
   * @param nickname the nickname that the client wants to use on the server.
   * @return a String in JSON format as JSONObject, used to be sent to the server to log in on it.
   */
  public static JSONObject login(String nickname) {
    try {
      return createMessageOfType(LOGIN).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message that is sent to a client if he successfully logged in on the server.
   *
   * @return a String in JSON format that can be sent to the client to inform him that he
   *         successfully logged in.
   */
  public static JSONObject loginSuccess() {
    try {
      return createMessageOfType(LOGIN_SUCCESS);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message that can be sent from the server to the client that informs him that his
   * request for log in was denied. It also provides a reason why the client could not log in.
   *
   * @param reasonForDeniedLogin the reason why the client could not log in.
   * @return a String in JSON format that can be sent to the client to inform him that he could not
   *         log in on the server and why.
   */
  public static JSONObject loginFailed(String reasonForDeniedLogin) {
    try {
      return createMessageOfType(LOGIN_FAILED).put(ADDITIONAL_INFORMATION, reasonForDeniedLogin);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message that the server can broadcast that a new user has successfully logged in on
   * it.
   *
   * @param nickname the nickname of the user that just logged in.
   * @return a String in JSON format telling the other clients that a new user logged in.
   */
  public static JSONObject userJoined(String nickname) {
    try {
      return createMessageOfType(USER_JOINED).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message that the server can broadcast that a user has logged out.
   *
   * @param nickname the nickname of the user that just logged out.
   * @return a String in JSON format telling the other clients that a user logged out (and who).
   */
  public static JSONObject userLeft(String nickname) {
    try {
      return createMessageOfType(USER_LEFT).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message that is sent from the client to the server requesting that the active player
   * places the chosen tile in the specified pattern line.
   *
   * @param rowOfPatternLine the index of the pattern line that the user has chosen.
   * @return a String in JSON format telling the server in which pattern line it should place the
   *         chosen tile.
   */
  public static JSONObject placeTileInPatternLine(int rowOfPatternLine) {
    try {
      return createMessageOfType(PLACE_TILE_IN_PATTERN_LINE)
          .put(INDEX_OF_PATTERN_LINE_FIELD, rowOfPatternLine);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a message that is sent from the client to the server requesting that the active player
   * places the chosen tile in the floor line.
   *
   * @return a String in JSON format telling the server that the chosen tile should be placed in
   *         floor line of the active player.
   */
  public static JSONObject placeTileInFloorLine() {
    try {
      return createMessageOfType(PLACE_TILE_IN_FLOOR_LINE);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Get the content of the message that a Client sent to the server.
   *
   * @param object the message from which we can to take the content.
   * @return the content of the message that the Client sent to the server.
   */
  public static String getContent(JSONObject object) {
    try {
      return object.getString(CONTENT_FIELD);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to read a json object.", e);
    }
  }

  /**
   * Create a message which posted between the client and the server.
   *
   * @param content The text should be posted.
   * @return A Message in JSON format.
   */
  public static JSONObject postMessage(String content) {
    try {
      JSONObject message = createMessageOfType(POST_MESSAGE);
      message.put(CONTENT_FIELD, content);

      return message;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }


  /**
   * Converts a date to a string.

   * @param date the date to be converted
   * @return the string
   */
  private static String convertDateToString(Date date) {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY)
        .format(date);
  }


  /**
   * converts a string to a date.

   * @param date string of a date to be converted.
   * @return the date as a date type
   * @throws ParseException the exception
   */
  private static Date convertStringToDate(String date) throws ParseException {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY)
        .parse(date);
  }

  /**
   * Create a JSON message of type cheat message, that is sent from the client to the server.
   *
   * @param content the cheat that the client wants to execute.
   * @return the message that the client can send to the server in order to execute the cheat.
   */
  public static JSONObject createCheatMessage(String content) {
    try {
      JSONObject cheatMessage = createMessageOfType(CHEAT_MESSAGE);
      cheatMessage.put(CONTENT_FIELD, content);
      return cheatMessage;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public String getJsonName() {
    return jsonName;
  }

  /**
   * Get the name of the player who sends message.
   *
   * @param object The JsonObject which used to get the name of sender.
   * @return The name of the player.
   */
  public static String getNickname(JSONObject object) {
    try {
      return object.getString(NICK_FIELD);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to read a json object.", e);
    }
  }

  /**
   * The time of the Message that sent by the player to the server and then to other players.
   *
   * @param object The JsonObject that used to get the time of the message sent by the players.
   * @return The time of the sending message.
   */
  public static Date getTime(JSONObject object) {
    try {
      String date = object.getString(TIME_FIELD);
      return convertStringToDate(date);
    } catch (ParseException e) {
      throw new IllegalArgumentException("Failed to parse the date from a json object.", e);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to read a json object.", e);
    }
  }




}
