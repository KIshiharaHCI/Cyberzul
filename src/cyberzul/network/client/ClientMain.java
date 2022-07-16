package cyberzul.network.client;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import cyberzul.view.CyberzulView;

/**
 * Starts the chat-client.
 *
 * <p>This is a test class.
 */
public class ClientMain {

  public static void main(String[] args) {

    Model model = new ModelStrategyChooser();
    model.setStrategy(Model.CLIENT_MODEL);
    Controller controller = new GameController(model);

    CyberzulView cyberzulView = new CyberzulView(model, controller);

    model.addPropertyChangeListener(cyberzulView);

    cyberzulView.setVisible(true);
  }
}
