package azul.team12.network.client;

import static java.util.Objects.requireNonNull;

import azul.team12.model.events.*;
import azul.team12.network.client.messages.Message;
import azul.team12.network.client.messages.PlayerTextMessage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientModel {

  private final PropertyChangeSupport support;
  private boolean loggedIn;
  private ClientNetworkConnection connection;
  private String nickname;

  private final List<Message> playerMessages;

  public ClientModel(){

    loggedIn = false;

    support = new PropertyChangeSupport(this);

    setConnection(new ClientNetworkConnection(this));

    playerMessages = new ArrayList<>();
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
  public void logInWithName(final String nickname) {
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

  /**
   * Notify the Listeners that one Player lefts the game.
   * @param nickname The name of the player who lefts the game.
   */
  public void playerLeft(final String nickname) {
    this.nickname = nickname;
    notifyListeners(new GameForfeitedEvent(nickname));

  }

  /**
   * Add a text message to the list of chat messages.
   * Used by the network layer to update the model accordingly.
   *
   * @param nickname The name of the player that has sent this message.
   * @param date     The date when the chat message was sent.
   * @param content  The actual content (text) that the player had sent.
   */
  public void addTextMessage(String nickname, Date date, String content) {
    PlayerTextMessage message = new PlayerTextMessage(nickname, date, content);
    playerMessages.add(message);
    notifyListeners(new PlayerAddedMessageEvent(message));
  }
}
