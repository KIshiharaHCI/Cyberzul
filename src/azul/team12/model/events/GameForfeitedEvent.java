package azul.team12.model.events;

/**
 * Informs the listeners that the game has finished. Also carries the information of the player
 * who has won.
 */
public class GameForfeitedEvent extends GameEvent{

  public final String FORFEITER;

  public GameForfeitedEvent(String nickname){
    this.FORFEITER = nickname;
  }

  public String getForfeiter() {
    return FORFEITER;
  }

  @Override
  public String getName(){
    return "GameForfeitedEvent";
  }
}
