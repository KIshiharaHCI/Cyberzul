package cyberzul.network.server;

import cyberzul.controller.Controller;
import cyberzul.controller.GameController;
import cyberzul.model.Model;
import cyberzul.model.ModelStrategyChooser;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The server on which the GameModel runs if the game is played on network mode. All clients connect
 * to this server. The server starts a ServerNetworkConnection, in which the socket is waiting for
 * clients to connect.
 */
public class Server {

  private static Server instance;
  private static String iPv4InHex;
  private final Model gameModel;
  private static ServerNetworkConnection connection;
  private static boolean isRunning;

  /**
   * The server on which the game runs and who distributes chat messages.
   * Suppress FBwarning because only one server will be created.
   */
  @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
  private Server() {
    this.gameModel = new ModelStrategyChooser();
    gameModel.setGameModelStrategy();
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
   * @return the IP address of the Server.
   */
  public static synchronized String start() {
    if (instance == null) {
      instance = new Server();
      iPv4InHex = getIpAsHex();
      isRunning = true;
    }
    return iPv4InHex;
  }

  /**
   * Returns the IPv4 address as hex String, because IPv4 is a 32-bit address, so the highest
   * possible address is 255.255.255.255, that are 15 characters. It is much more convenient for the
   * end user the number of digits he has to type in to 8. The dots are omitted, because we know
   * that every number between the dots is represented by exactly two characters in the hex String.
   *
   * @return the IPv4 address, encoded as String of hexadecimals.
   */
  public static String getIpAsHex() {
    StringBuilder ipinhex = new StringBuilder();
    try {
      String ipindecimals = InetAddress.getLocalHost().getHostAddress();
      String[] ipsplitup = ipindecimals.split("\\.");
      for (int i = 0; i < ipsplitup.length; i++) {
        int dec = Integer.parseInt(ipsplitup[i]);
        if (dec <= 15) {
          ipinhex.append("0" + Integer.toHexString(dec));
        } else {
          ipinhex.append(Integer.toHexString(dec));
        }
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return ipinhex.toString();
  }

  /**
   * Informs if the server is running right now.
   *
   * @return <code>true</code> if the server is running right now. <code>false</code> else.
   */
  public static boolean isRunning() {
    return isRunning;
  }

  /**
   * Stops the server and disposes of all resources.
   */
  public static void stop() {
    instance = null;
  }
}
