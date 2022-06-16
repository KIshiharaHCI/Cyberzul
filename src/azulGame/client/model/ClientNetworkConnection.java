package azulGame.client.model;

public class ClientNetworkConnection {

  private static final String HOST = "localhost";
  private static final int PORT = 8080;

  private final GameClientModel model;

  /**
   * creates the connection a client and the server.
   *
   * @param model          - the model of this client.
   */
  public ClientNetworkConnection(GameClientModel model) {
    this.model = model;
  }

}
