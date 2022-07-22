package cyberzul.network.client;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import cyberzul.view.CyberzulView;



/**
 * Starts a ClientModel and Client UI. The ClientModel tries then to connect itself with a Server
 */
public class ClientMain {

  /*
   * Create a new Model and tell it to behave as ClientModel. Also start a Controller and a View.
   *
   * @param args the parameters with which this method is invoked. Here are no parameters expected.
   */
  /*
  public static void main(String[] args) {

    Model model = new ModelStrategyChooser();
    model.setClientModelStrategy("");
    Controller controller = new GameController(model);

    CyberzulView cyberzulView = new CyberzulView(model, controller);

    model.addPropertyChangeListener(cyberzulView);

    cyberzulView.setVisible(true);
  }
  */
}
