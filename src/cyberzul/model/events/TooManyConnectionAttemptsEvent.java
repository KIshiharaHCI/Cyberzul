package cyberzul.model.events;

/**
 * Gets fired to the listeners if the Client tried to often to connect with the Server.
 */
public class TooManyConnectionAttemptsEvent extends GameEvent{

  public static final String EVENT_NAME = "TooManyConnectionAttemptsEvent";

  public String getName(){
    return EVENT_NAME;
  }
}
