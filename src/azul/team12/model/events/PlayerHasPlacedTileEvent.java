package azul.team12.model.events;

public class PlayerHasPlacedTileEvent extends GameEvent {


  private final String nickname;

  public PlayerHasPlacedTileEvent(String nickname){
    this.nickname = nickname;
  }

  public String getNickname(){
    return nickname;
  }

  @Override
  public String getName() {
    return "PlayerHasPlacedTileEvent";
  }

}
