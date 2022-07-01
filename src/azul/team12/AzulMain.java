package azul.team12;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.view.AzulView;
import azul.team12.model.Player;
import java.util.ArrayList;

public class AzulMain {
  public static void main(String[] args){
    GameModel model = new GameModel();
    Controller controller = new GameController(model);
    AzulView azulView = new AzulView();
    azulView.setVisible(true);


  }
}
