package azul.team12.model.events;

public class GameCanceledEvent extends GameEvent {

  public final String CANCLER;

  public GameCanceledEvent(String nickname){
    this.CANCLER = nickname;
  }

  public String getCancler() {
    return CANCLER;
  }

  @Override
  public String getName(){
    return "GameCanceledEvent";
  }
}
