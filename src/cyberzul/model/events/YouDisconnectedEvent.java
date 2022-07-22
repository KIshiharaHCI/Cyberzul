package cyberzul.model.events;

/**
 * Gets fired if the client got disconnected from the server.
 */
public class YouDisconnectedEvent extends GameEvent{

  public static final String EVENT_NAME = "YouDisconnectedEvent";

  public String getName(){
    return EVENT_NAME;
  }
}
