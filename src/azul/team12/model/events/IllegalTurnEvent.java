package azul.team12.model.events;

/**
 * Gets fired when a user tried to make an illegal turn.
 */
public class IllegalTurnEvent extends GameEvent {

  @Override
  public String getName() {
    return "IllegalTurnEvent";
  }
}
