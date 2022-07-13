package azul.team12.model.events;

/**
 * Informs the listeners that the game has been forfeited by one player. Also carries the
 * information of the player who has forfeited.
 */
public class GameForfeitedEvent extends GameEvent {

  public static final String EVENT_NAME = "GameForfeitedEvent";

  public String forfeiter;

  public GameForfeitedEvent(String nickname) {
    this.forfeiter = nickname;
  }

  public String getForfeiter() {
    return forfeiter;
  }

  @Override
  public String getName() {
    return GameForfeitedEvent.EVENT_NAME;
  }
}
