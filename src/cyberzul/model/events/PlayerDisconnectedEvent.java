package cyberzul.model.events;

/**
 * This event gets fired if a player leaves the server before the game was started. So he is not
 * replaced by an AI.
 */
public class PlayerDisconnectedEvent extends GameEvent {

  public static final String EVENT_NAME = "PlayerDisconnectedEvent";

  private final String nickname;

  public PlayerDisconnectedEvent(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public String getName() {
    return EVENT_NAME;
  }

  public String getNickname() {
    return nickname;
  }
}
