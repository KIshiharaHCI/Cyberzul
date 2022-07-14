package azul.team12.network.client;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.ModelStrategyChooser;
import azul.team12.view.AzulView;

/**
 * Starts the chat-client.
 *
 * This is a test class.
 */
public class ClientMain {

  public static void main(String[] args) {

    Model model = new ModelStrategyChooser();
    model.setStrategy(Model.CLIENT_MODEL);
    Controller controller = new GameController(model);
    
    AzulView azulView = new AzulView(model, controller);

    model.addPropertyChangeListener(azulView);

    azulView.setVisible(true);
  }
}

