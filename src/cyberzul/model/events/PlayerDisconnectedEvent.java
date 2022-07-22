package cyberzul.model.events;

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
