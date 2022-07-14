package azul.team12;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.model.ModelStrategyChooser;
import azul.team12.view.AzulView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AzulMain {

  private static final Logger LOGGER = LogManager.getLogger(AzulMain.class);

  public static void main(String[] args) {
    //TODO: Change to Interface Model, not GameModel.
    LOGGER.trace("Entering the AzulGame application");
    LOGGER.info("Logging something.");
    Model model = new ModelStrategyChooser();
    Controller controller = new GameController(model);
    AzulView azulView = new AzulView(model, controller);
    azulView.setVisible(true);

    model.addPropertyChangeListener(azulView);

    LOGGER.trace("Exiting application.");
  }
}
