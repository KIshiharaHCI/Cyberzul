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

  }

  @Override
  public void startGame() {

  }

  @Override
  public List<FactoryDisplay> getFactoryDisplays() {
    return null;
  }

  @Override
  public TableCenter getTableCenter() {
    return null;
  }

  @Override
  public String whosTurnIsIt() {
    return null;
  }

  @Override
  public int getPoints(String playerName) {
    return 0;
  }

  @Override
  public void endTurn() {

  }

  @Override
  public void chooseTileFrom(String playerName, int indexOfTile, Offering offering) {

  }

  @Override
  public boolean placeTileAtPatternLine(int indexOfPatternLine) {
    return false;
  }
}
