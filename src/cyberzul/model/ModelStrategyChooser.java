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
  public void setStrategy(int strategy) {
    switch (strategy) {
      case GAME_MODEL -> this.strategy = new GameModel();
      case CLIENT_MODEL -> this.strategy = new ClientModel();
      default -> throw new AssertionError("No such strategy defined");
    }
    addListenersToTheModel();
    isStrategyChosen = true;
  }

  /**
   * The ModelStrategyChooser saves the PropertyChangeListener that should be added to the model in
   * a list and adds them if they should be added.
   */
  private void addListenersToTheModel() {
    for (PropertyChangeListener listener : listenerList) {
      strategy.addPropertyChangeListener(listener);
    }
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

  @Override
  public void startSinglePlayerMode(int numberOfAiPlayers) {
    strategy.startSinglePlayerMode(numberOfAiPlayers);
  }

  @Override
  public void startTimerForPlayer(String playerName) {
    strategy.startTimerForPlayer(playerName);
  }

  @Override
  public String getHotSeatStory() {
    return strategy.getHotSeatStory();
  }

  @Override
  public String getNetworkStory() {
    return strategy.getNetworkStory();
  }

  @Override
  public String getSinglePlayerStory() {
    return strategy.getSinglePlayerStory();
  }
}
