package cyberzul.model.events;

/**
 * Informs the listener that a given player does not exist.
 */
public class PlayerDoesNotExistEvent extends GameEvent {

  private final String nickname;

  public PlayerDoesNotExistEvent(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return this.nickname;
  }

  @Override
  public String getName() {
    return "PlayerDoesNotExistEvent";
  }
}
