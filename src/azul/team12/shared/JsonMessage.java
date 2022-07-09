package azul.team12.shared;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import org.json.JSONException;
import org.json.JSONObject;

public enum JsonMessage {

  LOGIN("login"), LOGIN_SUCCESS("login success"), LOGIN_FAILED("login failed"),
  USER_JOINED("user joined"), POST_MESSAGE("post message"), MESSAGE("message"),
  USER_LEFT("user left"), CHEAT_MESSAGE("cheat message");

  public static final String TYPE_FIELD = "type";

  public static final String NICK_FIELD = "nick";

  public static final String CONTENT_FIELD = "content";

  public static final String TIME_FIELD = "time";

  public static final String ADDITIONAL_INFORMATION = "additional information";

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
      return createMessageOfType(LOGIN_FAILED).put(ADDITIONAL_INFORMATION,reasonForDeniedLogin);
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

  public static String getAdditionalInformation(JSONObject object){
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
