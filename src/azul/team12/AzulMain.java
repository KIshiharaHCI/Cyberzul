package azul.team12;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.network.client.ClientController;
import azul.team12.network.client.ClientModel;
import azul.team12.view.AzulView;
import azul.team12.model.Player;
import java.util.ArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

public class AzulMain {

  private static final Logger LOGGER = LogManager.getLogger(AzulMain.class);
  public static void main(String[] args){
    //TODO: Change to Interface Model, not GameModel.
    Configurator.initialize(new DefaultConfiguration());
    Configurator.setRootLevel(Level.INFO);
    LOGGER.trace("Entering the AzulGame application");
    LOGGER.info("Logging something.");
    //TODO: HIER WURDE GAMEMODEL ZU CLIENTMODEL UND GAMECONTROLLER ZU CLIENTCONTROLLER
    ClientModel model = new ClientModel();
    Controller controller = new ClientController(model);
    AzulView azulView = new AzulView(new GameModel(), controller);
    azulView.setVisible(true);

    model.addPropertyChangeListener(azulView);


    LOGGER.trace("Exiting application.");
  }
}
