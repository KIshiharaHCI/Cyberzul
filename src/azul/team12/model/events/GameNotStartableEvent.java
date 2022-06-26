package azul.team12.model.events;

/**
 * Gets fired if someone tries to start the game but not enough players have logged in yet.
 */
public class GameNotStartableEvent extends GameEvent{

  public static final String GAME_ALREADY_STARTED = "game already started";
  public static final String NOT_ENOUGH_PLAYER = "not enough player";

  private final String message;

  public GameNotStartableEvent(String message){
    switch (message){
      case GAME_ALREADY_STARTED -> this.message = GAME_ALREADY_STARTED;
      case NOT_ENOUGH_PLAYER -> this.message = NOT_ENOUGH_PLAYER;
      default -> throw new AssertionError("unknown message");
    }
  }

  @Override
  public String getName(){
    return "GameNotStartableEvent";
  }

  public String getMessage(){
    return this.message;
  }
}
