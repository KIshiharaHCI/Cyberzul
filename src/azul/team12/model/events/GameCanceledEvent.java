package azul.team12.model.events;

/**
 * Informs the listener that the game has been canceled. Also holds the information about the player
 * who has canceled the game.
 */
public class GameCanceledEvent extends GameEvent {

  public final String nameOfPersonThatCanceled;

  public static final String EVENT_NAME = "GameCanceledEvent";

  public GameCanceledEvent(String nickname) {
    this.nameOfPersonThatCanceled = nickname;
  }

  public String getNameOfPersonThatCanceled() {
    return nameOfPersonThatCanceled;
  }

  @Override
  public String getName() {
    return EVENT_NAME;
  }
}
