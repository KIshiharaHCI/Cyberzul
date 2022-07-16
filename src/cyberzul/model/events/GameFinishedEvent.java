package cyberzul.model.events;

/**
 * Informs the listeners that the game has finished. Also carries the information of the player who
 * has won.
 */
public class GameFinishedEvent extends GameEvent {

  public static final String EVENT_NAME = "GameFinishedEvent";

  public final String winner;

  /**
   * Constructs the event.

   * @param nickname with the nickname of the player.
   */
  public GameFinishedEvent(String nickname) {
    this.winner = nickname;
  }

  public String getWinner() {
    return winner;
  }

  @Override
  public String getName() {
    return GameFinishedEvent.EVENT_NAME;
  }
}
