package azul.team12;

import static java.lang.System.in;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import azul.team12.view.AzulView;
<<<<<<< HEAD
=======
import java.io.BufferedInputStream;
import java.io.InputStream;
>>>>>>> main
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

public class AzulMain {

  private static final Logger LOGGER = LogManager.getLogger(AzulMain.class);

  public static void main(String[] args) {
    //TODO: Change to Interface Model, not GameModel.
    LOGGER.trace("Entering the AzulGame application");
    LOGGER.info("Logging something.");
    Model model = new GameModel();
    Controller controller = new GameController(model);
    AzulView azulView = new AzulView(model, controller);
    azulView.setVisible(true);

    model.addPropertyChangeListener(azulView);

    LOGGER.trace("Exiting application.");
  }
}
