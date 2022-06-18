package azulGame.client.view.board;

import azulGame.client.view.board.playerBoard.PlayerBoard;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class GameBoard extends JFrame {



  public GameBoard() {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setSize (600, 600);
    PlayerBoard playerBoard = new PlayerBoard();
    add(playerBoard,BorderLayout.CENTER);
  }
}
