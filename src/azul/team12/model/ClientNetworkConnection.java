package azul.team12.model;

import static java.util.Objects.requireNonNull;

public class ClientNetworkConnection {

  private static final String HOST = "localhost";
  private static final int PORT = 8080;

  private final GameModel model;

  /**
   * creates the connection a client and the server.
   *
   * @param model - the model of this client.
   * @throws NullPointerException if passed model is <code>null</code>.
   */
  public ClientNetworkConnection(GameModel model) {
    this.model = requireNonNull(model);
  }

}
