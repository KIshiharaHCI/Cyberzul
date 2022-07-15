package azul.team12.model.events;

public class NotYourTurnEvent extends GameEvent {

  @Override
  public String getName() {
    return "NotYourTurnEvent";
  }
}