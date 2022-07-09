package azul.team12.network.client;

import azul.team12.controller.Controller;
import azul.team12.model.Model;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.Player;
import java.util.List;

public class ClientController implements Controller {

  Model model;

  public ClientController(Model model) {
    this.model = model;
  }

  @Override
  public void addPlayer(String name) {
    model.loginWithName(name);
  }

  @Override
  public void startGame() {

  }

  @Override
  public void restartGame() {

  }

  @Override
  public List<Offering> getOfferings() {
    return null;
  }

  @Override
  public List<Offering> getFactoryDisplays() {
    return null;
  }

  @Override
  public Offering getTableCenter() {
    return null;
  }

  @Override
  public String getNickOfActivePlayer() {
    return null;
  }

  @Override
  public String getNickOfNextPlayer() {
    return null;
  }

  @Override
  public int getPoints(String playerName) {
    return 0;
  }

  @Override
  public int getMinusPoints(String playerName) {
    return 0;
  }

  @Override
  public void chooseTileFrom(String playerName, int indexOfTile, int offeringIndex) {

  }

  @Override
  public void endTurn(String playerName) {

  }

  @Override
  public boolean placeTileAtPatternLine(int rowOfPatternLine) {
    return false;
  }

  @Override
  public void placeTileAtFloorLine() {

  }

  @Override
  public List<String> getPlayerNamesList() {
    return null;
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    return new ModelTile[0][];
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    return null;
  }

  @Override
  public ModelTile[][] getWallOfPlayerAsTiles(String playerName) {
    return new ModelTile[0][];
  }

  @Override
  public ModelTile[][] getTemplateWall() {
    return new ModelTile[0][];
  }

  @Override
  public List<Player> rankingPlayerWithPoints() {
    return null;
  }

  @Override
  public void forfeitGame() {

  }
}
