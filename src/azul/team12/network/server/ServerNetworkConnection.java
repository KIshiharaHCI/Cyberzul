package azul.team12.network.server;

import azul.team12.controller.Controller;
import azul.team12.model.Model;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/**
 * The network layer of the chat server. Takes care of processing both the connection requests and
 * message handling.
 */
public class ServerNetworkConnection {

  private static final int PORT = 2307;
  private static final byte[] HOST = new byte[] {0,0,0,0};
  private static final int BACKLOG = 10;

  private final ExecutorService executorService;

  private final ServerSocket socket;

  private final List<ClientMessageHandler> clientHandlers;

  private Model model;
  private Controller controller;
  private ModelPropertyChangeHandler modelPropertyChangeHandler;

  private final Runnable connectionAcceptor = new Runnable() {

    @Override
    public void run() {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          Socket clientSocket = socket.accept();
          ClientMessageHandler handler =
              new ClientMessageHandler(ServerNetworkConnection.this, clientSocket, controller,
                  model);
          addHandler(handler);
          executorService.execute(handler);
        }
      } catch (IOException e) {
        System.out.println("Socket got interrupted");
        // Thrown when the socket gets interrupted
      }
    }
  };

  /**
   * Set up a new {@link ServerNetworkConnection} that handles all the incoming client connection
   * requests and enables them to exchange messages with each other.
   *
   * @throws IOException thrown when the socket is unable to be created at the given port
   */
  public ServerNetworkConnection(Model gameModel, Controller controller) throws IOException {
    this.model = gameModel;
    this.controller = controller;
    executorService = Executors.newCachedThreadPool();
    //socket = new ServerSocket(PORT);
    socket = new ServerSocket(PORT,BACKLOG, InetAddress.getByAddress(HOST));
    clientHandlers = Collections.synchronizedList(new ArrayList<>());

    modelPropertyChangeHandler = new ModelPropertyChangeHandler(this, model);
  }

  public synchronized void sendToActivePlayer(JSONObject object) {
    try {
      String activePlayerName = model.getNickOfActivePlayer();
      for (ClientMessageHandler handler : clientHandlers) {
        if (handler.getNickname().equals(activePlayerName) && handler.isLoggedIn()) {
          handler.send(object);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for a single client to broadcast a message to all the other connected clients. The
   * broadcast goes to everyone except the sending handler.
   *
   * @param sender  The client from whom the message originates
   * @param message The message as JSONObject that is to be broadcast.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  public void broadcast(ClientMessageHandler sender, JSONObject message) throws IOException {
    synchronized (clientHandlers) {
      for (ClientMessageHandler handler : clientHandlers) {
        if (handler != sender && handler.isLoggedIn()) {
          handler.send(message);
        }
      }
    }
  }

  /**
   * Broadcasts a message to all clients.
   *
   * @param message The message as JSONObject that is to be broadcast.
   * @throws IOException Thrown when failing to access the input- or output-stream.
   */
  public void broadcastToAll(JSONObject message) throws IOException {
    synchronized (clientHandlers) {
      for (ClientMessageHandler handler : clientHandlers) {
        handler.send(message);
      }
    }
  }

  /**
   * Add a new handler to the list of connected clients.
   *
   * @param handler The new {@link ClientMessageHandler handler}
   */
  public void addHandler(ClientMessageHandler handler) {
    synchronized (clientHandlers) {
      clientHandlers.add(handler);
    }
  }

  /**
   * Check if a chosen nickname is still available.
   *
   * @param nickname The name to be looked up.
   * @return <code>true</code> if no other client has taken this name, <code >false</code> otherwise.
   */
  public synchronized boolean tryLogIn(String nickname) {
    synchronized (clientHandlers) {
      for (ClientMessageHandler handler : clientHandlers) {
        if (nickname.equals(handler.getNickname())) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * Start the network-connection, so that clients can establish a connection to this server.
   */
  public void start() {
    executorService.execute(connectionAcceptor);
  }

  /**
   * Stop the network-connection.
   */
  public void stop() {
    executorService.shutdownNow();
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void handlerClosed(ClientMessageHandler handler) {
    clientHandlers.remove(handler);
  }

  public void handlerClosed(String nickname){
    ClientMessageHandler toRemove = null;
    for(ClientMessageHandler handler : clientHandlers){
      if(handler.getNickname().equals(nickname)){
        toRemove = handler;
        break;
      }
    }
    handlerClosed(toRemove);
  }
}
