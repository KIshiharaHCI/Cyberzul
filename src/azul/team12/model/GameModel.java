package azul.team12.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class GameModel {

  private final PropertyChangeSupport support;
  private ArrayList<Player> playerList;


  public GameModel() {
    support = new PropertyChangeSupport(this);
  }

  public void loginWithName(String nickname) {
    Player newPlayer = new Player(nickname);
    playerList.add(newPlayer);
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

}
