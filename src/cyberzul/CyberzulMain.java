package cyberzul;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import cyberzul.view.CyberzulView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CyberzulMain {

    private static final Logger LOGGER = LogManager.getLogger(CyberzulMain.class);

    public static void main(String[] args) {
        //TODO: Change to Interface Model, not GameModel.
        LOGGER.trace("Entering the AzulGame application");
        LOGGER.info("Logging something.");
        Model model = new ModelStrategyChooser();
        Controller controller = new GameController(model);
        CyberzulView cyberzulView = new CyberzulView(model, controller);
        cyberzulView.setVisible(true);

        model.addPropertyChangeListener(cyberzulView);

        LOGGER.trace("Exiting application.");
    }
}
