package cyberzul.model.events;

/**
 * Informs the listeners that a player has ended the game. Also carries the information of the
 * player who has ended the game.
 */
public class PlayerHas5TilesInArowEvent extends GameEvent {

  public static final String EVENT_NAME = "PlayerHas5TilesInARowEvent";

  public final String ender;

  public PlayerHas5TilesInArowEvent(String nickname) {
    this.ender = nickname;
  }

  public String getEnder() {
    return ender;
  }

  @Override
  public String getName() {
    return EVENT_NAME;
  }
}
