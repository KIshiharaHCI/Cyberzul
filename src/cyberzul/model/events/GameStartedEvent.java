package cyberzul.model.events;

/**
 * Informs the listeners that the game has started.
 */
public class GameStartedEvent extends GameEvent {

  public static final String EVENT_NAME = "GameStartedEvent";

  @Override
  public String getName() {
    return EVENT_NAME;
  }
}
