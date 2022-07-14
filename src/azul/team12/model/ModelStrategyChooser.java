package azul.team12.model;

import azul.team12.network.client.ClientModel;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ModelStrategyChooser implements Model{

  private ModelStrategy strategy;

  @Override
  public void setStrategy(int strategy) {
    switch (strategy){
      case GAME_MODEL -> this.strategy = new GameModel();
      case CLIENT_MODEL -> this.strategy = new ClientModel();
      default -> throw new AssertionError("No such strategy defined");
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    strategy.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    strategy.removePropertyChangeListener(listener);
  }

  @Override
  public void loginWithName(String nickname) {
    strategy.loginWithName(nickname);
  }

  @Override
  public void startGame() {
    strategy.startGame();
  }

  @Override
  public void restartGame() {
    strategy.restartGame();
  }

  @Override
  public void cancelGame() {
    strategy.cancelGame();
  }

  @Override
  public void replacePlayerByAi(String playerName) {
    strategy.replacePlayerByAi(playerName);
  }

  @Override
  public String getPlayerName() {
    return strategy.getPlayerName();
  }

  @Override
  public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {
    strategy.notifyTileChosen(playerName,indexOfTile,offeringIndex);
  }

  @Override
  public void makeActivePlayerPlaceTile(int rowOfPatternLine) {
    strategy.makeActivePlayerPlaceTile(rowOfPatternLine);
  }

  @Override
  public void tileFallsDown() {
    strategy.tileFallsDown();
  }

  @Override
  public boolean checkRoundFinished() {
    return strategy.checkRoundFinished();
  }

  @Override
  public int getIndexOfPlayerWithSpm() {
    return strategy.getIndexOfPlayerWithSpm();
  }

  @Override
  public String getPlayerWithMostPoints() {
    return strategy.getPlayerWithMostPoints();
  }

  @Override
  public List<String> rankingPlayerWithPoints() {
    return strategy.rankingPlayerWithPoints();
  }

  @Override
  public int getIndexOfActivePlayer() {
    return strategy.getIndexOfActivePlayer();
  }

  @Override
  public int getIndexOfNextPlayer() {
    return strategy.getIndexOfNextPlayer();
  }

  @Override
  public Player getPlayerByName(String nickname) {
    return strategy.getPlayerByName(nickname);
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    return strategy.getPatternLinesOfPlayer(playerName);
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    return strategy.getFloorLineOfPlayer(playerName);
  }

  @Override
  public ModelTile[][] getWallOfPlayer(String playerName) {
    return strategy.getWallOfPlayer(playerName);
  }

  @Override
  public List<String> getPlayerNamesList() {
    return strategy.getPlayerNamesList();
  }

  @Override
  public List<Offering> getOfferings() {
    return strategy.getOfferings();
  }

  @Override
  public int getPoints(String nickname) {
    return strategy.getPoints(nickname);
  }

  @Override
  public int getMinusPoints(String nickname) {
    return strategy.getMinusPoints(nickname);
  }

  @Override
  public String getNickOfActivePlayer() {
    return strategy.getNickOfActivePlayer();
  }

  @Override
  public boolean isGameStarted() {
    return strategy.isGameStarted();
  }
}
