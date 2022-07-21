package cyberzul.model.events;

/**
 * Informs the listeners that the game has finished. Also carries the information
 * with the winning message.
 */
public class GameFinishedEvent extends GameEvent {

  public static final String EVENT_NAME = "GameFinishedEvent";

  public final String winningMessage;

  /**
   * Constructs the event.
   *
   * @param winningMessage with the winningMessage of the player.
   */
  public GameFinishedEvent(String winningMessage) {
    this.winningMessage = winningMessage;
  }

  public String getWinningMessage() {
    return winningMessage;
  }

  @Override
  public String getName() {
    return GameFinishedEvent.EVENT_NAME;
  }
}
