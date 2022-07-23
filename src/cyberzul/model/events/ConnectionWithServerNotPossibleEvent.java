package cyberzul.model.events;

/**
 * Gets fired to the listeners if the Client tried to often to connect with the Server.
 */
public class ConnectionWithServerNotPossibleEvent extends GameEvent {

  public static final String EVENT_NAME = "ConnectionWithServerNotPossibleEvent";

  public String getName() {
    return EVENT_NAME;
  }
}
