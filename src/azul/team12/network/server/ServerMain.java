package azul.team12.network.server;

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
    final ServerNetworkConnection connection = new ServerNetworkConnection();
    connection.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        connection.stop();
      }
    });
  }
}