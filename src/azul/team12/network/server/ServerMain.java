package azul.team12.network.server;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.GameModel;
import azul.team12.model.Model;
import java.io.IOException;

/**
 * The main class of the azul server. It starts the application to let clients connect themselves
 * to this server, and give him instructions on how to run the game.
 *
 * This class will be deleted later on, so only one main-method is left in this program. It is used
 * for test and development only.
 */
public class ServerMain {

  /**
   * Launch the azul server.
   */
  public static void main(String[] args) throws IOException {
    Model gameModel = new GameModel();
    Controller controller = new GameController(gameModel);
    final ServerNetworkConnection connection = new ServerNetworkConnection(gameModel,controller);
    connection.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        connection.stop();
      }
    });
  }
}