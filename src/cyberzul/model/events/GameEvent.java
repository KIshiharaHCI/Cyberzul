package cyberzul.model.events;

/** The events that the model gives out to its listeners to tell them about the game state. */
public abstract class GameEvent {

  /**
   * The name of the event as string representation.
   *
   * @return a string describing the implementing event.
   */
  public abstract String getName();
}
