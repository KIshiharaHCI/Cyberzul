package cyberzul.model.events;

/**
 * Informs the listener that it is not his turn.
 */
public class NotYourTurnEvent extends GameEvent {

  @Override
  public String getName() {
    return "NotYourTurnEvent";
  }
}
