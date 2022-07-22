package cyberzul.model.events;

/**
 * Gets fired to the listeners if the client successfully connected to the server.
 */
public class YouConnectedEvent extends GameEvent{

  public static final String EVENT_NAME = "YouConnectedEvent";

  public String getName(){
    return EVENT_NAME;
  }
}
