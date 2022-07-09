package azul.team12.shared;

import azul.team12.model.Offering;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;

public enum JsonMessage {

  //chat messages
  LOGIN("login"), LOGIN_SUCCESS("login success"), LOGIN_FAILED("login failed"),
  PlAYER_JOINED("player joined"), POST_MESSAGE("post message"), MESSAGE("message"),
  PLAYER_LEFT("Player left"), CHEAT_MESSAGE("cheat message"),

  //Controller methods
  CHOOSE_TILE_FROM("choose tile from"),
  END_TURN("end turn"),
  FORFEIT("forfeit"),
  GET_NICK_OF_ACTIVE_PLAYER("get nick of active player"),
  GET_NICK_OF_NEXT_PLAYER("get nick of next player"),
  GET_OFFERINGS("get offerings"),
  GET_PATTERN_LINES_OF_PLAYER("get pattern lines of player"),
  GET_PLAYER_NAMES_LIST("get player names list"),
  GET_POINTS("get points"),
  GET_TEMPLATE_WALL("get template wall"),
  GET_WALL_OF_PLAYER_AS_TILES("get wall of player as tiles"),
  PLACE_TILE_AT_FLOOR_LINE("place tile at floor line"),
  PLACE_TILE_AT_PATTERN_LINE("place tile at pattern line"),
  RANKING_PLAYER_WITH_POINTS("ranking player with points"),
  START_GAME("start game"),

  //Methods from the Server
  GAME_STARTED("game started");

  public static final String TYPE_FIELD = "type";

  public static final String NICK_FIELD = "nick";

  public static final String CONTENT_FIELD = "content";

  public static final String TIME_FIELD = "time";

  public static final String ADDITIONAL_INFORMATION = "additional information";

  public static final String INDEX_OF_TILE_FIELD = "index of tile ";

  public static final String INDEX_OF_OFFERING_FIELD = "index of offering";

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
  public static JSONObject chooseTileFrom(int indexOfTile, int indexOfOffering) {
    try {
      JSONObject jsonObject = createMessageOfType(CHOOSE_TILE_FROM);
      jsonObject.put(INDEX_OF_TILE_FIELD, indexOfTile);
      jsonObject.put(INDEX_OF_OFFERING_FIELD, indexOfOffering);
      return jsonObject;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * Create a JSONObject by passing only the name of the method.
   * I.e. "createJSONMessage(END_TURN);
   *
   * @param methodName the name of the method in the Controller Interface.
   * @return a JSONObject containing the information that this specific method was invoked.
   */
  public static JSONObject createJSONmessage(JsonMessage methodName) {
    try {
      return createMessageOfType(methodName);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
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
  public static JSONObject createJSONmessage(JsonMessage methodName, String nickname) {
    try {
      return createMessageOfType(methodName).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  /**
   * This message is created by the server in order to inform the clients of the content of all
   * Offerings.
   *
   * @return A JSONObject that contains the TableCenter at index 0 and FactoryDisplays at all other
   * positions.
   */
  //TODO: OFFERINGS ZURÜCKGEBEN
  /*
  public static JSONObject createMessageWithAllOfferings() {
    try {

    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

   */


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

  public static JSONObject playerJoined(String nickname) {
    try {
      return createMessageOfType(PlAYER_JOINED).put(NICK_FIELD, nickname);
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  public static JSONObject playerLeft(String nickname) {
    try {
      return createMessageOfType(PLAYER_LEFT).put(NICK_FIELD, nickname);
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

  public static JSONObject createCheatMessage(String nickname, String content) {
    try {
      JSONObject cheatMessage = createMessageOfType(CHEAT_MESSAGE);
      cheatMessage.put(CONTENT_FIELD, content);
      cheatMessage.put(NICK_FIELD, nickname);

      return cheatMessage;
    } catch (JSONException e) {
      throw new IllegalArgumentException("Failed to create a json object.", e);
    }
  }

  private static JSONObject createMessageOfType(JsonMessage type) throws JSONException {
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
