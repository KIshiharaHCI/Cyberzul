package cyberzul.model.events;

/** Informs the view that there is no valid turn to make. Game should be restarted. */
public class NoValidTurnToMakeEvent extends GameEvent {

  @Override
  public String getName() {
    return "NoValidTurnToMakeEvent";
  }
}
