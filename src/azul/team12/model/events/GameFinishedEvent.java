package azul.team12.model.events;

/**
 * Informs the listeners that the game has finished. Also carries the information of the player who
 * has won.
 */
public class GameFinishedEvent extends GameEvent {

  public final String WINNER;

  public GameFinishedEvent(String nickname) {
    this.WINNER = nickname;
  }

  public String getWINNER() {
    return WINNER;
  }

  @Override
  public String getName() {
    return "GameFinishedEvent";
  }
}
