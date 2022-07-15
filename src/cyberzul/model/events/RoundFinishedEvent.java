package cyberzul.model.events;

/**
 * The round is finished. That means every tile was taken from every Factory Display and the Table
 * Center.
 */
public class RoundFinishedEvent extends GameEvent {

  @Override
  public String getName() {
    return "RoundFinishedEvent";
  }
}
