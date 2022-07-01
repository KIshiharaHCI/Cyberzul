package azul.team12;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.view.AzulView;
import azul.team12.model.Player;
import java.util.ArrayList;

public class AzulMain {
  public static void main(String[] args){
    //TODO: Change to Interface Model, not GameModel.
    GameModel model = new GameModel();
    Controller controller = new GameController(model);
    AzulView azulView = new AzulView(model, controller);
    azulView.setVisible(true);

    model.addPropertyChangeListener(azulView);


  }
}
