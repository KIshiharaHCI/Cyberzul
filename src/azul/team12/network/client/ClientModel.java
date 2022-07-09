package azul.team12.network.client;

import static java.util.Objects.requireNonNull;

import azul.team12.model.Model;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.Player;
import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.LoggedInEvent;
import azul.team12.model.events.LoginFailedEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class ClientModel implements Model {

  private final PropertyChangeSupport support;
  private boolean loggedIn;
  private ClientNetworkConnection connection;
  private String nickname;

  public ClientModel(){

    loggedIn = false;

    support = new PropertyChangeSupport(this);

    setConnection(new ClientNetworkConnection(this));
  }

  /**
   * Add a {@link PropertyChangeListener} to the model to get notified about any changes that the
   * the model publishes.
   *
   * @param listener the view that subscribes itself to the model.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

  /**
   * Remove a listener from the model. It will then no longer get notified about any events
   * fired by the model.
   *
   * @param listener the view that is to be unsubscribed from the model.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  @Override
  public void startGame() {

  }

  @Override
  public void restartGame() {

  }

  @Override
  public void forfeitGame() {

  }

  @Override
  public void endTurn() {

  }

  @Override
  public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {

  }

  @Override
  public boolean makeActivePlayerPlaceTile(int rowOfPatternLine) {
    return false;
  }

  @Override
  public void tileFallsDown() {

  }

  @Override
  public boolean checkRoundFinished() {
    return false;
  }

  @Override
  public int getIndexOfPlayerWithSPM() {
    return 0;
  }

  @Override
  public void startTilingPhase() {

  }

  @Override
  public String getPlayerWithMostPoints() {
    return null;
  }

  @Override
  public List<Player> rankingPlayerWithPoints() {
    return null;
  }

  @Override
  public int getIndexOfNextPlayer() {
    return 0;
  }

  @Override
  public Player getPlayerByName(String nickname) {
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
  public ModelTile[][] getWallOfPlayer(String playerName) {
    return new ModelTile[0][];
  }

  @Override
  public List<String> getPlayerNamesList() {
    return null;
  }

  @Override
  public List<Offering> getOfferings() {
    return null;
  }

  @Override
  public int getIndexOfActivePlayer() {
    return 0;
  }

  @Override
  public int getPoints(String nickname) {
    return 0;
  }

  @Override
  public int getMinusPoints(String nickname) {
    return 0;
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

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed.
   */
  private void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  private synchronized ClientNetworkConnection getConnection() {
    return connection;
  }

  /**
   * Add a network connector to this model.
   *
   * @param connection The network connection to be added.
   */
  public synchronized void setConnection(ClientNetworkConnection connection) {
    this.connection = connection;
  }

  public synchronized boolean isLoggedIn() {
    return loggedIn;
  }

  public synchronized void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  /**
   * Send a login request to the server.
   *
   * @param nickname the chosen nickname of the chat participant.
   */
  public void loginWithName(final String nickname) {
    this.nickname = nickname;
    getConnection().sendLogin(nickname);
  }

  /**
   * Update the model accordingly when a login attempt is successful. This is afterwards published
   * to the subscribed listeners.
   */
  public void loggedIn() {
    setLoggedIn(true);
    notifyListeners(new LoggedInEvent());
  }

  /**
   * Notify the subscribed observers that a login attempt has failed.
   */
  public void loginFailed(String message) {
    setLoggedIn(false);
    notifyListeners(new LoginFailedEvent(message));
  }

  /**
   * Notify the listeners that the game has started.
   */
  public void gameStarted(){
    notifyListeners(new GameStartedEvent());
  }
}
