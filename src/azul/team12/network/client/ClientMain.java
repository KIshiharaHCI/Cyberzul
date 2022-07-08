package azul.team12.network.client;

import azul.team12.model.GameModel;
import azul.team12.view.AzulView;

/**
 * Starts the chat-client.
 *
 * This is a test class.
 */
public class ClientMain {

  public static void main(String[] args) {

    ClientModel model = new ClientModel();
    ClientController controller = new ClientController(model);
    
    AzulView azulView = new AzulView(new GameModel(), controller);

    model.addPropertyChangeListener(azulView);

    ClientNetworkConnection connection = new ClientNetworkConnection(model);
    model.setConnection(connection);
    connection.start();

    azulView.setVisible(true);
  }
}

