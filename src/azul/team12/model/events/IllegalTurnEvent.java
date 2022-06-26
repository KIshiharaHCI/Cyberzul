package azul.team12.model.events;

public class IllegalTurnEvent extends GameEvent{

  @Override
  public String getName() {
    return "IllegalTurnEvent";
  }
}
