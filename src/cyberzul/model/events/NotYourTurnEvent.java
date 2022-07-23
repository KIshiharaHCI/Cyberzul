package cyberzul.model.events;

/**
 * Informs the listener that it is not his turn.
 */
public class NotYourTurnEvent extends GameEvent {

  private static final String CHAT_MESSAGE =
      "We know you are excited to play the game! But please wait for other players to make \n"
          + "their moves first.\n";

  @Override
  public String getName() {
    return "NotYourTurnEvent";
  }

  public String getChatMessage() {
    return CHAT_MESSAGE;
  }
}
