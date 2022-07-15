package azul.team12.model.events;

/**
 * This event gets fired from the model to the listeners, if another user joined the server.
 */
public class UserJoinedEvent extends GameEvent {

  public static final String EVENT_NAME = "UserJoinedEvent";
  private final String nickOfUserWhoJoined;

  public UserJoinedEvent(String nickOfUserWhoJoined) {
    this.nickOfUserWhoJoined = nickOfUserWhoJoined;
  }

  public String getName() {
    return EVENT_NAME;
  }

}
