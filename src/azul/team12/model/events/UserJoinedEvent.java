package azul.team12.model.events;

/**
 * This event gets fired from the model to the listeners, if another user joined the server.
 */
public class UserJoinedEvent extends GameEvent{

  private final String nickOfUserWhoJoined;
  public static final String EVENT_NAME = "UserJoinedEvent";

  public UserJoinedEvent(String nickOfUserWhoJoined){
    this.nickOfUserWhoJoined = nickOfUserWhoJoined;
  }

  public String getName(){
    return EVENT_NAME;
  }

}
