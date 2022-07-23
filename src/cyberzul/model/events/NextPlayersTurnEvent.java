package cyberzul.model.events;

/**
 * Gives the view the information that the next player has to make his turn now. The view can get
 * the information about the name of the player who's turn will be next.
 */
public class NextPlayersTurnEvent extends GameEvent {

  private final String nameOfActivePlayer;
  private final String nameOfPlayerWhoEndedHisTurn;

  public NextPlayersTurnEvent(String nameOfActivePlayer, String nameOfPlayerWhoEndedHisTurn) {
    this.nameOfActivePlayer = nameOfActivePlayer;
    this.nameOfPlayerWhoEndedHisTurn = nameOfPlayerWhoEndedHisTurn;
  }

  public String getNameOfActivePlayer() {
    return nameOfActivePlayer;
  }

  @Override
  public String getName() {
    return "NextPlayersTurnEvent";
  }

  public String getNameOfPlayerWhoEndedHisTurn() {
    return nameOfPlayerWhoEndedHisTurn;
  }

  /**
   * Creates the Message that is shown on the Chat and tells the players who is next.
   *
   * @return the message that tells the players who's turn is now.
   */
  public String getChatMessage(){
    return ("It's " + nameOfActivePlayer + "'s turn.");
  }
}
