package cyberzul.model.events;

/**
 * Gets fired when a user tried to make an illegal turn.
 */
public class IllegalTurnEvent extends GameEvent {

  private static final String CHAT_MESSAGE =
      "Mighty tiler,\n"
      + "Please keep the following rules passed on from generation to generation in mind:\n\n"
      + "- You shall not place tiles in a pattern line, that already holds tiles of a \n"
      + "different color. \n"
      + "- You shall not place tiles in a pattern line, that already has a tile of the chosen \n"
      + "color activated on the wall. \n\n"
      + "There is no place left for you? You cannot place your chosen tile(s) anywhere? \n"
      + "Reconsider you choice … or … place the chosen tile(s) directly in your floor line.";

  @Override
  public String getName() {
    return "IllegalTurnEvent";
  }

  public String getChatMessage() {
    return CHAT_MESSAGE;
  }

}
