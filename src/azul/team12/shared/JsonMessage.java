package azul.team12.shared;

import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
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

public enum JsonMessage {

  LOGIN("login"), LOGIN_SUCCESS("login success"), LOGIN_FAILED("login failed"),
  USER_JOINED("user joined"), POST_MESSAGE("post message"), MESSAGE("message"),
  USER_LEFT("user left"),

  //messages from the client to the server
  END_TURN("end turn"),
  FORFEIT_GAME("forfeit game"),
  START_GAME("start game"),
  RESTART_GAME("restart game"),
  NOTIFY_TILE_CHOSEN("notify tile chosen"),
  PLACE_TILE_IN_PATTERN_LINE("place tile in pattern line"),
  PLACE_TILE_IN_FLOOR_LINE("place tile in floor line"),

  //messages from the server to the client
  GAME_STARTED("game started"),
  NOT_YOUR_TURN("not your turn"),
  NEXT_PLAYERS_TURN("next players turn"),
  PLAYER_HAS_CHOSEN_TILE("player has chosen tile"),
  NO_VALID_TURN_TO_MAKE("no valid turn to make");

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

  private final String jsonName;

  JsonMessage(String jsonName) {
    this.jsonName = jsonName;
  }

  public static JsonMessage typeOf(JSONObject message) {
    String typeName;
    try {
      typeName = message.getString(TYPE_FIELD);
    } catch (JSONException e) {
      throw new IllegalArgumentException(String.format("Unknown message type '%s'", message), e);
    }

    Optional<JsonMessage> opt =
        Arrays.stream(JsonMessage.values()).filter(x -> x.getJsonName().equals(typeName))
            .findFirst();
    return opt.orElseThrow(
        () -> new IllegalArgumentException(String.format("Unknown message type '%s'", typeName)));
  }

  //Controller methods

  /**
   * Create a JSONMessage that tells the server which tile the player choose and from which
   * offering.
   *
   * @param indexOfTile     the index of the tile in the Offering.
   * @param indexOfOffering <code>0</code> means the TableCenter. Every following index is a FactoryDisplay.
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
  public static JSONObject createGameStartedMessage(List<Offering> offerings,
                                                    List<String> playerNames) {
    try {
      JSONObject returnObject = createMessageOfType(GAME_STARTED);
      returnObject.put(OFFERINGS_FIELD, parseOfferingsToJSONArray(offerings));
      returnObject.put(PLAYER_NAMES_FIELD, parsePlayerNamesToJSONArray(playerNames));

      return returnObject;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * This message gets created by the server to inform the clients that the active player has
   * chosen a tile.
   *
   * @return a JSONObject that can be distributed via output streams.
   */
  public static JSONObject createPlayerHasChosenTileMessage(String nameOfActivePlayer) {
    try {
      JSONObject message = createMessageOfType(PLAYER_HAS_CHOSEN_TILE);
      message.put(NAME_OF_ACTIVE_PLAYER_FIELD,nameOfActivePlayer);
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
  public static JSONArray parseOfferingsToJSONArray(List<Offering> offerings) {
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
   * Creates a JSONArray that contains the
   *
   * @param playerNames
   * @return
   */
  public static JSONArray parsePlayerNamesToJSONArray(List<String> playerNames) {
    JSONArray playerNamesArray = new JSONArray();
    for (String nick : playerNames) {
      playerNamesArray.put(nick);
    }
    return playerNamesArray;
  }

  //TODO: WRITE THIS METHOD

  /**
   * This method is created by the server to notify the players that a player has done his turn
   * and a new player has to do his turn now.
   * It contains all information about what changes occurred during the past turn.
   *
   * @param offerings
   * @param nameOfPlayerWhoEndedHisTurn
   * @param newPatternLinesOfPlayerWhoEndedHisTurn
   * @param newFloorLineOfPlayerWhoEndedHisTurn
   * @param indexOfPlayerWithSPM
   * @return
   */
  public static JSONObject createNextPlayersTurnMessage(String nameOfActivePlayer,
                                                        List<Offering> offerings,
                                                        String nameOfPlayerWhoEndedHisTurn,
                                                        ModelTile[][] newPatternLinesOfPlayerWhoEndedHisTurn,
                                                        List<ModelTile> newFloorLineOfPlayerWhoEndedHisTurn,
                                                        int indexOfPlayerWithSPM) {
    try {
      JSONObject returnObject = createMessageOfType(NEXT_PLAYERS_TURN);

      returnObject.put(NAME_OF_ACTIVE_PLAYER_FIELD, nameOfActivePlayer);

      returnObject.put(OFFERINGS_FIELD, parseOfferingsToJSONArray(offerings));

      returnObject.put(NAME_OF_PLAYER_WHO_ENDED_HIS_TURN_FIELD, nameOfPlayerWhoEndedHisTurn);

      returnObject.put(PATTERN_LINES_FIELD,
          parsePatternLinesToJSONArray(newPatternLinesOfPlayerWhoEndedHisTurn));

      returnObject.put(FLOOR_LINE_FIELD,
          parseFloorLineToJSONArray(newFloorLineOfPlayerWhoEndedHisTurn));

      returnObject.put(INDEX_OF_PLAYER_WITH_SPM, indexOfPlayerWithSPM);

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
  private static JSONArray parsePatternLinesToJSONArray(ModelTile[][] patternLines) {
    JSONArray patternLinesArray = new JSONArray();
    for (int row = 0; row < patternLines.length; row++) {
      JSONArray line = new JSONArray();
      for (int col = 0; col < patternLines[0].length; col++) {
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
  private static JSONArray parseFloorLineToJSONArray(List<ModelTile> floorLine) {
    JSONArray floorLineArray = new JSONArray();
    for (ModelTile t : floorLine) {
      floorLineArray.put(t);
    }
    return floorLineArray;
  }

  /**
   * Create a JSONObject by passing only the name of the method and the nick of the player.
   * I.e. "createJSONMessage(GET_POINTS,jonas123);
   *
   * @param methodName the name of the method in the Controller Interface.
   * @param nickname   the nickname of the player that is passed to the method.
   * @return a JSONObject containing the information that this specific method was invoked with the
   * name of the player with whom the method was invoked.
   */
  public static JSONObject createMessageOfType(JsonMessage methodName, String nickname) {
    try {
      return createMessageOfType(methodName).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject login(String nickname) {
    try {
      return createMessageOfType(LOGIN).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject loginSuccess() {
    try {
      return createMessageOfType(LOGIN_SUCCESS);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject loginFailed(String reasonForDeniedLogin) {
    try {
      return createMessageOfType(LOGIN_FAILED).put(ADDITIONAL_INFORMATION, reasonForDeniedLogin);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject userJoined(String nickname) {
    try {
      return createMessageOfType(USER_JOINED).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject userLeft(String nickname) {
    try {
      return createMessageOfType(USER_LEFT).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject placeTileInPatternLine(int rowOfPatternLine) {
    try {
      return createMessageOfType(PLACE_TILE_IN_PATTERN_LINE).put(INDEX_OF_PATTERN_LINE_FIELD,
          rowOfPatternLine);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject placeTileInFloorLine() {
    try {
      return createMessageOfType(PLACE_TILE_IN_FLOOR_LINE);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject postMessage(String content) {
    try {
      JSONObject message = createMessageOfType(POST_MESSAGE);
      message.put(CONTENT_FIELD, content);

      return message;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

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

  public static JSONObject createMessageOfType(JsonMessage type) throws JSONException {
    return new JSONObject().put(TYPE_FIELD, type.getJsonName());
  }

  public static String getNickname(JSONObject object) {
    try {
      return object.getString(NICK_FIELD);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to read a json object.", e);
    }
  }

  public static String getAdditionalInformation(JSONObject object) {
    try {
      return object.getString(ADDITIONAL_INFORMATION);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to read a json object.", e);
    }
  }

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

  public static String getContent(JSONObject object) {
    try {
      return object.getString(CONTENT_FIELD);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to read a json object.", e);
    }
  }

  private static String convertDateToString(Date date) {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY)
        .format(date);
  }

  private static Date convertStringToDate(String date) throws ParseException {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.GERMANY)
        .parse(date);
  }

  public String getJsonName() {
    return jsonName;
  }
}
