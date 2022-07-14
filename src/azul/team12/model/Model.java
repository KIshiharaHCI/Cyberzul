package azul.team12.model;

import azul.team12.model.events.GameFinishedEvent;
import azul.team12.model.events.GameForfeitedEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import azul.team12.model.events.PlayerHasEndedTheGameEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The main interface of the Azul model for the graphical user-interface. It provides all necessary
 * methods for accessing and manipulating the data such that a game can be played successfully.
 */
public interface Model extends ModelStrategy{

  int GAME_MODEL = 0;
  int CLIENT_MODEL = 1;

  /**
   * Tell the ModelStrategyChooser what kind of strategy he should use.
   *
   * @param strategy
   */
  void setStrategy(int strategy);
}
