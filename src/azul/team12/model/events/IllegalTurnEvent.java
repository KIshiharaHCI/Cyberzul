package azul.team12.model.events;

//TODO: @Marco @Nils add JavaDoc
public class IllegalTurnEvent extends GameEvent{

  @Override
  public String getName() {
    return "IllegalTurnEvent";
  }
}
