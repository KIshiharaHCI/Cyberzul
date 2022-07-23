package cyberzul.model.events;

/**
 * Informs the listeners that the game has started.
 */
public class GameStartedEvent extends GameEvent {

  private final String activePlayer;

  public GameStartedEvent(String activePlayer) {
    this.activePlayer = activePlayer;
  }

  public static final String EVENT_NAME = "GameStartedEvent";

  private static final String CHAT_MESSAGE =
      "Hello fellow cyber tiler!,\n"
          + "We welcome you to a new game of Cyberzul! \n"
          + "May Tilora, the goddess of home building and cyber tiling, be with you! \n"
          + "Before starting your game: \n"
          + "Please check out the instructions in our README-file. \n"
          + "Ok! Let’s go! Click a tile on one of the Cyber Mines \n"
          + "and place it on one of your pattern lines! \n";

  @Override
  public String getName() {
    return EVENT_NAME;
  }

  /**
   * Creates the Message that is shown on the Chat after the game started.
   *
   * @return the message to the players after starting the game.
   */
  public String getChatMessage() {
    return (CHAT_MESSAGE +  "It's " + activePlayer + "'s turn.");
  }
}
