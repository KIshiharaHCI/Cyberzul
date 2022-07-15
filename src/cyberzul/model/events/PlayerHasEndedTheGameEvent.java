package cyberzul.model.events;

/**
 * Informs the listeners that a player has ended the game. Also carries the information of the
 * player who has ended the game.
 */
public class PlayerHasEndedTheGameEvent extends GameEvent {

  public final String ender;

  public PlayerHasEndedTheGameEvent(String nickname) {
    this.ender = nickname;
  }

  public String getEnder() {
    return ender;
  }

  @Override
  public String getName() {
    return "PlayerHasEndedTheGameEvent";
  }

}
