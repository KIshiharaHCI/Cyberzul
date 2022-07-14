package azul.team12.network.server;

import azul.team12.controller.Controller;
import azul.team12.controller.GameController;
import azul.team12.model.Model;
import azul.team12.model.ModelStrategyChooser;
import azul.team12.model.TableCenter;
import java.io.IOException;

public class Server {

  private Model gameModel;
  private static Server instance;
  private ServerNetworkConnection connection;

  private Server(){
    this.gameModel = new ModelStrategyChooser();
    gameModel.setStrategy(Model.GAME_MODEL);
    Controller controller = new GameController(gameModel);
    try {
      connection = new ServerNetworkConnection(gameModel, controller);
      connection.start();
      Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
          connection.stop();
        }
      });
    }
    catch (IOException e){
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
    }
    else{
      throw new RuntimeException("It already exists a server with this port");
    }
    return instance;
  }
}
