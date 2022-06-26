package azul.team12.model.events;

/**
 * Informs the listeners that a player was successfully added to the game.
 */
public class PlayerAddedEvent extends GameEvent{

  public final String nickname;

  public PlayerAddedEvent(String nickname){
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }

  @Override
  public String getName() {
    return "PlayerAddedEvent";
  }
}
