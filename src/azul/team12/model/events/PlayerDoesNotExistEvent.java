package azul.team12.model.events;

public class PlayerDoesNotExistEvent extends GameEvent{

  private final String nickname;

  public PlayerDoesNotExistEvent(String nickname){
    this.nickname = nickname;
  }

  public String getNickname(){
    return this.nickname;
  }

  @Override
  public String getName() {
    return "PlayerDoesNotExistEvent";
  }
}
