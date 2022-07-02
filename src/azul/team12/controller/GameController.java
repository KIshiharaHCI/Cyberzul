package azul.team12.controller;

import azul.team12.model.GameModel;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.WallBackgroundPattern;
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
  public String getNickOfNextPlayer() {
    return model.getPlayerNamesList().get(model.getIndexOfNextPlayer(model.getIndexOfActivePlayer()));
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
    model.notifyTileChosen(playerName, indexOfTile, offering);
    System.out.println("Player " + playerName + " has chosen the " + indexOfTile + ". Tile from Offering " + offering.toString());
  }

  @Override
  public boolean placeTileAtPatternLine(int rowOfPatternLine) {
    System.out.println("Player " + getNickOfActivePlayer() + " tries to place a tile on the " + rowOfPatternLine + ". row of pattern lines.");
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

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    return model.getPatternLinesOfPlayer(playerName);
  }

  @Override
  public boolean[][] getWallOfPlayer(String playerName) {
    return model.getWallOfPlayer(playerName);
  }

  @Override
  public ModelTile[][] getTemplateWall() {
    return WallBackgroundPattern.getTemplateWall();
  }

}
