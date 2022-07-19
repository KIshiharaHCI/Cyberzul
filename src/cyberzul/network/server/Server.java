package cyberzul.network.server;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import java.io.IOException;

/**
 * The server on which the GameModel runs if the game is played on network mode.
 * All clients connect to this server.
 * The server starts a ServerNetworkConnection, in which the socket is waiting for clients to
 * connect.
 */
public class Server {

  private static Server instance;
  private final Model gameModel;
  private ServerNetworkConnection connection;

  /**
   * The server on which the game runs and who distributes chat messages.
   */
  private Server() {
    this.gameModel = new ModelStrategyChooser();
    gameModel.setStrategy(Model.GAME_MODEL);
    Controller controller = new GameController(gameModel);
    try {
      connection = new ServerNetworkConnection(gameModel, controller);
      connection.start();
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread() {
                @Override
                public void run() {
                  connection.stop();
                }
              });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is used so this class can be a Singleton: Only one single instance of this class
   * can be created. If it already exists, the existing instance is returned.
   *
   * @return a new Server, if it doesn't exist already. The instance to the existing one else.
   */
  public static synchronized Server start() {
    if (instance == null) {
      instance = new Server();
    } else {
      throw new RuntimeException("It already exists a server with this port");
    }
    return instance;
  }
}
