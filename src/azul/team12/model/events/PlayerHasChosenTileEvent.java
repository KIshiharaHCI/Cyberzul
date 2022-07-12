package azul.team12.model.events;

/**
 * Gives the view the information that the next player has to make his turn now. The view can get
 * the information about the name of the player who's turn will be next.
 */
public class PlayerHasChosenTileEvent extends GameEvent {

  private final String nickname;

  public PlayerHasChosenTileEvent(String nickname) {
    this.nickname = nickname;
  }

  public String getNickname() {
    return nickname;
  }

  @Override
  public String getName() {
    return "PlayerHasChosenTileEvent";
  }

}
