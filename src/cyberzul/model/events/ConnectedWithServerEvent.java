package cyberzul.model.events;

/**
 * This event gets fired from the model to the listeners as soon as it connects successfully with
 * the server.
 */
public class ConnectedWithServerEvent extends GameEvent {

  public static final String EVENT_NAME = "ConnectedWithServerEvent";

  public String getName() {
    return EVENT_NAME;
  }
}
