package azul.team12.model.events;

/**
 * Informs the listeners that the game has started.
 */
public class GameStartedEvent extends GameEvent {

  @Override
  public String getName() {
    return "GameStartedEvent";
  }
}
