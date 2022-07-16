package cyberzul.network.server;

/**
 * Builds the messages that are broadcast to every client and that contain the information about the
 * content of the TableCenter, the content of the Factory Displays, the content of the wall, the
 * content of the
 */
public enum GameInfoJSONMessage {
  LOGIN("login"),
  LOGIN_SUCCESS("login success"),
  LOGIN_FAILED("login failed"),
  USER_JOINED("user joined"),
  POST_MESSAGE("post message"),
  MESSAGE("message"),
  USER_LEFT("user left");

  // GameInfoJSONMessages should always have the type GameInfo
  public static final String TYPE_FIELD = "type";

  // get Points, getFactoryDisplays, drawTiles, etc...
  public static final String SUBTYPE_FIELD = "subtype";

  private final String jsonName;

  GameInfoJSONMessage(String jsonName) {
    this.jsonName = jsonName;
  }
}
