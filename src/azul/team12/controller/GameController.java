package azul.team12.controller;

import azul.team12.model.Model;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.Player;
import azul.team12.model.WallBackgroundPattern;
import java.util.List;

public class GameController implements Controller {

  private Model model;

  public GameController(Model model) {
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
  public void restartGame() {
    model.restartGame();
  }

  @Override
  public List<Offering> getOfferings() {
    return model.getOfferings();
  }

  @Override
  public String getNickOfActivePlayer() {
    return model.getNickOfActivePlayer();
  }

  @Override
  public String getNickOfNextPlayer() {
    return model.getPlayerNamesList().get(model.getIndexOfNextPlayer());
  }

  @Override
  public int getPoints(String playerName) {
    return model.getPoints(playerName);
  }

  @Override
  public int getMinusPoints(String playerName) {
    return model.getMinusPoints(playerName);
  }

  @Override
  public void endTurn(String playerName) {
    model.endTurn();
  }

  @Override
  public void chooseTileFrom(String playerName, int indexOfTile, int offeringIndex) {
    model.notifyTileChosen(playerName, indexOfTile, offeringIndex);
  }

  @Override
  public boolean placeTileAtPatternLine(int rowOfPatternLine) {
    System.out.println(
        "Player " + getNickOfActivePlayer() + " tries to place a tile on the " + rowOfPatternLine
            + ". row of pattern lines.");
    return model.makeActivePlayerPlaceTile(rowOfPatternLine);
  }

  public void placeTileAtFloorLine() {
    model.tileFallsDown();
  }

  @Override
  public List<String> getPlayerNamesList() {
    return model.getPlayerNamesList();
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    return model.getPatternLinesOfPlayer(playerName);
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    return model.getFloorLineOfPlayer(playerName);
  }

  @Override
  public ModelTile[][] getWallOfPlayerAsTiles(String playerName) {
    return model.getWallOfPlayer(playerName);
  }

  @Override
  public ModelTile[][] getTemplateWall() {
    return WallBackgroundPattern.getTemplateWall();
  }

  @Override
  public List<Player> rankingPlayerWithPoints() {
    return model.rankingPlayerWithPoints();
  }

  @Override
  public void replaceActivePlayerByAI() {
    model.replaceActivePlayerByAi();
  }

  @Override
  public void cancelGameForAllPlayers() {
    model.cancelGame();
  }

}
