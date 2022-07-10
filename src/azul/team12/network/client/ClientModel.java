package azul.team12.network.client;

import static java.util.Objects.requireNonNull;

import azul.team12.model.ClientFactoryDisplay;
import azul.team12.model.ClientTableCenter;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.ModelTile;
import azul.team12.model.Offering;
import azul.team12.model.Player;
import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.LoggedInEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import azul.team12.shared.JsonMessage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class ClientModel implements Model {

  private final PropertyChangeSupport support;
  private boolean loggedIn;
  private ClientNetworkConnection connection;
  private String thisPlayersName;

  private int indexOfActivePlayer = 0;
  private ArrayList<ClientPlayer> playerList = new ArrayList<>();
  protected ArrayList<Offering> offerings;


  public ClientModel() {

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
    connection.send(JsonMessage.START_GAME);
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
    return true;
  }

  @Override
  public void tileFallsDown() {

  }

  @Override
  public void startTilingPhase() {

  }

  @Override
  public String getPlayerWithMostPoints() {
    //TODO: What if two players have the same points?
    ArrayList<Integer> playerPoints = new ArrayList<>();
    for (Player player : playerList) {
      playerPoints.add(player.getPoints());
    }
    int highestScore = Collections.max(playerPoints);
    int bestIndex = playerPoints.indexOf(highestScore);
    Player playerWithMostPoints = playerList.get(bestIndex);
    return playerWithMostPoints.getName();
  }

  @Override
  public List<String> rankingPlayerWithPoints() {
    //TODO: FOR TEST PURPOSES SIMPLIFIED
    return new ArrayList<>();
  }

  @Override
  public int getIndexOfNextPlayer() {
    //TODO: FOR TEST PURPOSES SIMPLIFIED
    return 0;
  }

  @Override
  public Player getPlayerByName(String nickname) {
    for(Player player : playerList) {
      if(player.getName().equals(nickname)) {
        return player;
      }
    }
    return null;
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getPatternLines();
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getFloorLine();
  }

  @Override
  public ModelTile[][] getWallOfPlayer(String playerName) {

    Player player = getPlayerByName(playerName);
    ModelTile[][] templateWall = player.getWallPattern().pattern;
    boolean[][] wallAsBools = player.getWall();
    ModelTile[][] playerWall = new ModelTile[5][5];

    for (int row = 0; row < wallAsBools.length; row++) {
      for (int col = 0; col < wallAsBools[row].length; col++) {
        if (wallAsBools[row][col]) {
          playerWall[row][col] = templateWall[row][col];
        } else {
          playerWall[row][col] = ModelTile.EMPTY_TILE;
        }
      }
    }

    return playerWall;
  }

  @Override
  public List<String> getPlayerNamesList() {
    List<String> list = new ArrayList<>();
    for (Player player: playerList) {
      list.add(player.getName());
    }
    return list;
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
    this.thisPlayersName = nickname;
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
   * Update all information in the model that is needed by the view to start showing the game.
   * Then notify the listeners that the game has started.
   */
  public void gameStarted(JSONArray offerings, JSONArray playerNames) throws JSONException{
    updateOfferings(offerings);
    setUpClientPlayersByName(playerNames);
    indexOfActivePlayer = 0;
    notifyListeners(new GameStartedEvent());
  }

  /**
   * Intitializes the data structure that contains the information about each player.
   *
   * @param playerNames
   */
  private void setUpClientPlayersByName(JSONArray playerNames) throws JSONException {
    for (int i = 0; i < playerNames.length(); i++) {
      ClientPlayer clientPlayer = new ClientPlayer(playerNames.getString(i));
      playerList.add(clientPlayer);
    }
    //TODO: TEST SOUT
    System.out.println(playerList);
  }

  @Override
  public List<Offering> getOfferings() {
    return (List<Offering>) offerings.clone();
  }

  @Override
  public int getPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  @Override
  public int getMinusPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getMinusPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  @Override
  public List<Offering> getFactoryDisplays(){
    // return the factory displays being the all but the first offering
    return offerings.subList(1, offerings.size());
  }

  @Override
  public Offering getTableCenter() {
    return offerings.get(0);
  }

  @Override
  public String getNickOfActivePlayer(){
    return playerList.get(indexOfActivePlayer).getName();
  }

  /**
   * Update the Offerings in the Model.
   *
   * @param offerings
   */
  private void updateOfferings(JSONArray offerings) throws JSONException {
    ArrayList<Offering> returnOfferingsList = new ArrayList<>();

    //update TableCenter
    ArrayList<ModelTile> content = new ArrayList<>();
    JSONArray tableCenterArray = offerings.getJSONArray(0);
    for(int i = 0; i < tableCenterArray.length(); i++){
      content.add(ModelTile.toTile(tableCenterArray.getString(i)));
    }
    ClientTableCenter clientTableCenter = new ClientTableCenter();
    clientTableCenter.setContent(content);
    returnOfferingsList.add(clientTableCenter);

    //update FactoryDisplays
    for (int i = 1; i < offerings.length(); i++) {
      content = new ArrayList<>();
      JSONArray factoryDisplayArray = offerings.getJSONArray(i);
      for(int j = 0; j < factoryDisplayArray.length(); j++){
        content.add(ModelTile.toTile(factoryDisplayArray.getString(j)));
      }
      ClientFactoryDisplay clientFactoryDisplay = new ClientFactoryDisplay();
      clientFactoryDisplay.setContent(content);
      returnOfferingsList.add(clientFactoryDisplay);
    }
    this.offerings = returnOfferingsList;

    //TODO: TEST SOUT:
    System.out.println(this.offerings.get(0).getContent().toString() + this.offerings.get(1).getContent().toString());
  }
}

