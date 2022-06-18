package azulGame.client.view;

import azulGame.client.controller.GameController;
import azulGame.client.model.ClientNetworkConnection;
import azulGame.client.model.GameClientModel;
import azulGame.client.view.board.GameBoard;

/**
 * starts the game client.
 */
public class GameClient {

  /**
   * todo - JavaDoc
   */
  public static void main(String[] args) {

    GameClientModel model = new GameClientModel();
    GameController controller = new GameController(model);
    //GameFrame gameFrame = new GameFrame(controller, model);
    GameBoard gameBoard = new GameBoard();
    //model.addPropertyChangeListener(gameFrame);

    ClientNetworkConnection connection = new ClientNetworkConnection(model);
    //model.setConnection(connection);
    //connection.start();

    //chatFrame.setVisible(true);
    gameBoard.setVisible(true);
  }

}

