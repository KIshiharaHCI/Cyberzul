package azul.team12.model.events;

/**
 * Gets fired to the listeners if a player unsuccessfully tries to log into the game.
 */
public class LoginFailedEvent extends GameEvent{

  public static final String LOBBY_IS_FULL = "Lobby is full.";
  public static final String NICKNAME_ALREADY_TAKEN = "Nickname already taken.";
  public static final String ALREADY_LOGGED_IN = "You are already logged in.";

  private final String message;

  public LoginFailedEvent(String message){
    switch(message){
      case LOBBY_IS_FULL -> this.message = LOBBY_IS_FULL;
      case NICKNAME_ALREADY_TAKEN -> this.message = NICKNAME_ALREADY_TAKEN;
      default -> throw new AssertionError("This message is unknown to this event.");
    }
  }

  @Override
  public String getName() {
    return "LoginFailedEvent";
  }

  public String getMessage(){
    return message;
  };
}
