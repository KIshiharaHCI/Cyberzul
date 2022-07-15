package cyberzul.model.events;

/**
 * The game is in an illegal state. No player can make a move. (For example - neither tiles on the
 * table center nor on the factory displays, because to many tiles have been put on the floor
 * line.)
 */
public class GameInIllegalStateEvent extends GameEvent {

  public static final String EVENT_NAME = "GameInIllegalStateEvent";

  private static final String MESSAGE = "You have arrived at an illegal state, where no "
      + "player can make another turn. The game will be ended and you can restart it.";

  public static String getMessage() {
    return MESSAGE;
  }

  @Override
  public String getName() {
    return GameInIllegalStateEvent.EVENT_NAME;
  }
}
