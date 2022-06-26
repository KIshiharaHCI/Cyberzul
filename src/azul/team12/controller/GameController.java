package azul.team12.controller;

import azul.team12.model.FactoryDisplay;
import azul.team12.model.GameModel;
import azul.team12.model.Offering;
import azul.team12.model.TableCenter;
import java.util.List;

public class GameController implements Controller{

  private GameModel model;

  public GameController(GameModel model){
    this.model = model;
  }

  @Override
  public void addPlayer(String name) {
    model.loginWithName(name);
  }

  @Override
  public void startGame() {
    model.startGame();
  }

  @Override
  public List<Offering> getFactoryDisplays() {
    return model.getFactoryDisplays();
  }

  @Override
  public Offering getTableCenter() {
    return model.getTableCenter();
  }

  @Override
  public String getNickOfActivePlayer() {
    return null;
  }

  @Override
  public int getPoints(String playerName) {
    return 0;
  }

  @Override
  public void endTurn(String playerName) {

  }

  @Override
  public void chooseTileFrom(String playerName, int indexOfTile, Offering offering) {

  }

  @Override
  public boolean placeTileAtPatternLine(int indexOfPatternLine) {
    return false;
  }
}
