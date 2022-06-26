package azul.team12.model;

import static java.util.Objects.requireNonNull;

import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;

  private final PropertyChangeSupport support;
  private ArrayList<Player> playerList;
  private ArrayList<Offering> factoryDisplays;
  private boolean isGameStarted = false;
  private int indexOfActivePlayer = 0;


  public GameModel() {
    support = new PropertyChangeSupport(this);
  }

  /**
   * Add a {@link PropertyChangeListener} to the model for getting notified about any changes that
   * are published by this model.
   *
   * @param listener the view that subscribes itself to the model.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

  /**
   * Remove a listener from the model. From then on it will no longer get notified about any events
   * fired by the model.
   *
   * @param listener the view that is to be unsubscribed from the model.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link azul.team12.model.events.GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed.
   *
   * @param event A concrete implementation of {@link azul.team12.model.events.GameEvent}
   */
  private void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  /**
   * A player trys to log in. Fires {@link LoginFailedEvent} if that was not possible.
   *
   * @param nickname the name that the player chose with his login attempt.
   */
  public void loginWithName(String nickname) {
    boolean nicknameFree = true;
    if (playerList.size() >= MAX_PLAYER_NUMBER) {
      notifyListeners(new LoginFailedEvent(LoginFailedEvent.LOBBY_IS_FULL));
    } else {
      for (Player player : playerList) {
        if (player.getName().equals(nickname)) {
          nicknameFree = false;
          notifyListeners(new LoginFailedEvent(LoginFailedEvent.NICKNAME_ALREADY_TAKEN));
        }
      }
      if (nicknameFree) {
        Player newPlayer = new Player(nickname);
        playerList.add(newPlayer);
      }
    }
  }

  /**
   * Tries to start the game. Fires {@link GameNotStartableEvent} if that is
   * not possible.
   */
  public void startGame() {
    if (playerList.size() < MIN_PLAYER_NUMBER) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (isGameStarted) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.GAME_ALREADY_STARTED));
    }
    else{
      int numberOfFactoryDisplays = (playerList.size() * 2) + 1;
      for(int i = 0; i < numberOfFactoryDisplays; i++){
        factoryDisplays.add(new FactoryDisplay());
      }
      notifyListeners(new GameStartedEvent());
    }
  }

  public List<Offering> getFactoryDisplays(){
    return (List<Offering>) factoryDisplays.clone();
  }

  public Offering getTableCenter(){
    return TableCenter.getInstance();
  }

  /**
   * Returns the nickname of the player who has to make his turn.
   *
   * @return the nickname of the player who has to make his turn.
   */
  public String getNickOfActivePlayer(){
    return playerList.get(indexOfActivePlayer).getName();
  }

  /**
   * Returns the number of points the player with the specified nickname has.
   *
   * @param nickname name of the player.
   * @return the points this player has.
   */
  public int getPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  public void endTurn(){

  }

}
