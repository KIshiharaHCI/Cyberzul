package azul.team12.controller;

import azul.team12.model.GameModel;
import azul.team12.model.Offering;
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
  public List<Offering> getOfferings() { return model.getOfferings(); }

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
    return model.getNickOfActivePlayer();
  }

  @Override
  public int getPoints(String playerName) {
    return model.getPoints(playerName);
  }

  @Override
  public void endTurn(String playerName) {
    model.endTurn();
  }

  @Override
  public void chooseTileFrom(String playerName, int indexOfTile, Offering offering) {
    System.out.println("Player " + playerName + "has chosen the " + indexOfTile + ". Tile from Offering " + offering.toString());
    model.notifyTileChosen(playerName, indexOfTile, offering);
  }

  @Override
  public boolean placeTileAtPatternLine(int rowOfPatternLine) {
    return model.makeActivePlayerPlaceTile(rowOfPatternLine);
  }

  @Override
  public List<String> getPlayerNamesList() {
    return model.getPlayerNamesList();
  }

  @Override
  public void startTiling() {
    model.startTilingPhase();
  }
}
