package cyberzul;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import cyberzul.view.CyberzulView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class of the Cyberzul project. It starts the application.
 */
public class CyberzulMain {

  private static final Logger LOGGER = LogManager.getLogger(CyberzulMain.class);

  /**
   * Creates the a new model, for which a strategy will be chosen, creates a controller
   * and the view.
   *
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    LOGGER.trace("Entering the AzulGame application");
    LOGGER.info("We are going in!");
    Model model = new ModelStrategyChooser();
    Controller controller = new GameController(model);
    CyberzulView cyberzulView = new CyberzulView(model, controller);
    cyberzulView.setVisible(true);

    model.addPropertyChangeListener(cyberzulView);

    LOGGER.trace("Exiting application.");
  }
}
