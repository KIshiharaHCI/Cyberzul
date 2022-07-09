package azul.team12.network.server;

import azul.team12.shared.JsonMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * Handles the events that are fired from the GameModel to the Server.
 *
 * Some messages are only broadcasted to the active player while others are broadcasted to everyone.
 */
public class ModelPropertyChangeHandler implements PropertyChangeListener {

  private ServerNetworkConnection connection;

  public ModelPropertyChangeHandler(ServerNetworkConnection connection) {
    this.connection = connection;
  }


  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          handleModelUpdate(event);
        }
        catch (IOException e){
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Handles the events that get fired from the model to the listeners of the model.
   *
   * @param event the event that was fired from the model.
   */
  private void handleModelUpdate(PropertyChangeEvent event) throws IOException {
    Object customMadeGameEvent = event.getNewValue();

    String eventName = event.getPropertyName();

    switch (eventName) {
      case "GameStartedEvent" -> connection.broadcastToAll(JsonMessage.createJSONmessage(JsonMessage.GAME_STARTED));
    }
  }
}
