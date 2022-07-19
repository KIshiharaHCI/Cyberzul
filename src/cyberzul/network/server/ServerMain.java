package cyberzul.network.server;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import java.io.IOException;

/**
 * This is the class that contains the main method to start a cyberzul server.
 */
public class ServerMain {

  /**
   * Launch the azul server.
   */
  public static void main(String[] args) {
    Server.start();
  }
}
