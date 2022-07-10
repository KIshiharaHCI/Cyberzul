package azul.team12.network.server;

import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.Offering;
import azul.team12.shared.JsonMessage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.json.JSONException;

/**
 * Handles the events that are fired from the GameModel to the Server.
 * <p>
 * Some messages are only broadcasted to the active player while others are broadcasted to everyone.
 */
public class ModelPropertyChangeHandler implements PropertyChangeListener {

  private ServerNetworkConnection connection;
  private Model model;

  public ModelPropertyChangeHandler(ServerNetworkConnection connection, Model model) {
    this.connection = connection;
    this.model = model;
    model.addPropertyChangeListener(this);
  }


  @Override
  public void propertyChange(PropertyChangeEvent event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          handleModelUpdate(event);
        } catch (IOException e) {
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
        case "GameStartedEvent" -> {
          //TODO: TEST SOUT
          System.out.println("Game Started in ModelPropertyChangeHandler");
          List<Offering> offerings = model.getOfferings();
          List<String> playerNames = model.getPlayerNamesList();
          connection.broadcastToAll(JsonMessage.createGameStartedMessage(offerings,playerNames));}
        default -> throw new AssertionError("Unknown event: " + eventName);
      }
  }
}
