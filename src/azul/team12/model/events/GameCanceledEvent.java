package azul.team12.model.events;

/**
 * Informs the listener that the game has been canceled. Also holds the information about the player
 * who has canceled the game.
 */
public class GameCanceledEvent extends GameEvent {

  public final String CANCLER;

  public GameCanceledEvent(String nickname) {
    this.CANCLER = nickname;
  }

  public String getCancler() {
    return CANCLER;
  }

  @Override
  public String getName() {
    return "GameCanceledEvent";
  }
}
