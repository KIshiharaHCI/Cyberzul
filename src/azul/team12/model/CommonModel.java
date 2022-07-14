package azul.team12.model;

import static java.util.Objects.requireNonNull;

import azul.team12.model.events.GameEvent;
import azul.team12.network.client.ClientPlayer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * Contains methods and fields that are common within Model classes.
 */
public abstract class CommonModel implements Model{

  protected int indexOfActivePlayer;
  protected ArrayList<Player> playerList = new ArrayList<>();
  protected ArrayList<Offering> offerings;
  protected final PropertyChangeSupport support;

  public CommonModel(){
    support = new PropertyChangeSupport(this);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed.
   * {@link azul.team12.model.events.GameEvent} gets fired such that the attached observers (i.e.,
   * {@link PropertyChangeListener}) can distinguish between what exactly has changed.
   *
   * @param event A concrete implementation of {@link GameEvent}
   */
  protected void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }
}
