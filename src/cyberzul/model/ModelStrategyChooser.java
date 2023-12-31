package cyberzul.model;

import cyberzul.network.client.ClientModel;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The Context of the Strategy Design Pattern. Depending on what behaviour the model should have,
 * the object type that is stored in the strategy field is determined at runtime.
 */
public class ModelStrategyChooser implements Model {

  private final List<PropertyChangeListener> listenerList = new ArrayList<>();
  private ModelStrategy strategy;
  private boolean isStrategyChosen = false;

  @Override
  public void setGameModelStrategy() {
    this.strategy = new GameModel(listenerList);
    isStrategyChosen = true;
  }

  @Override
  public void setClientModelStrategy(String ipAddress) {
    this.strategy = new ClientModel(listenerList, ipAddress);
    isStrategyChosen = true;
  }

  /**
   * Saves the view in a field, until it knows which model it should emulate. Then if the model gets
   * created, it adds the view as its PropertyChangeListener.
   *
   * @param listener the view that subscribes itself to the model.
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    if (isStrategyChosen) {
      strategy.addPropertyChangeListener(listener);
    }
    this.listenerList.add(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    this.listenerList.remove(listener);
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
    strategy.notifyTileChosen(playerName, indexOfTile, offeringIndex);
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
  public String getWinningMessage() {
    return strategy.getWinningMessage();
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

  @Override
  public void startSinglePlayerMode(int numberOfAiPlayers) {
    strategy.startSinglePlayerMode(numberOfAiPlayers);
  }

  @Override
  public void postChatMessage(String text) {
    strategy.postChatMessage(text);
  }

  @Override
  public void setBulletMode(boolean isBulletMode) {
    strategy.setBulletMode(isBulletMode);
  }

  @Override
  public boolean getBulletMode() {
    return strategy.getBulletMode();
  }

  @Override
  public void setMode(int mode) {
    strategy.setMode(mode);
  }

  @Override
  public int getMode() {
    return strategy.getMode();
  }

  @Override
  public boolean isStrategyChosen() {
    return isStrategyChosen;
  }
}
