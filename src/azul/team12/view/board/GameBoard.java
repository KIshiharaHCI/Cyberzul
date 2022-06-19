package azul.team12.view.board;

import azul.team12.view.board.playerBoard.PlayerBoard;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoard extends JPanel {

  List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;
  private int numberOfPlayers = 4;

  public GameBoard(GridBagLayout gridBagLayout) {
    super(gridBagLayout);



    for (int i = 0; i < numberOfPlayers - 1; i++) {
      GridBagConstraints gameBoardConstrains = new GridBagConstraints();
      gameBoardConstrains.insets = new Insets(10,10,10,10);
      gameBoardConstrains.fill = GridBagConstraints.HORIZONTAL;
      gameBoardConstrains.gridx = 0;
      gameBoardConstrains.gridy = i;
      gameBoardConstrains.gridwidth = 1;
      gameBoardConstrains.gridheight = 1;
      PlayerBoard playerBoard = new PlayerBoard();
      playerBoardList.add(playerBoard);
      add(playerBoard, gameBoardConstrains);
    }
    GridBagConstraints playerBoardCons = new GridBagConstraints();
    playerBoardCons.insets = new Insets(10,10,10,10);
    playerBoardCons.fill = GridBagConstraints.HORIZONTAL;
    playerBoardCons.gridx = 1;
    playerBoardCons.gridy = 1;
    playerBoardCons.gridwidth = 6;
    playerBoardCons.gridheight = 3;
    currentPlayerBoard = new PlayerBoard(300,600);
    add(currentPlayerBoard, playerBoardCons);






    //PlayerBoard playerBoard = new PlayerBoard();
    //add(playerBoard,BorderLayout.CENTER);
  }
}
