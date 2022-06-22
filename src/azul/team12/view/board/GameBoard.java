package azul.team12.view.board;

import azul.team12.view.board.playerBoard.PlayerBoard;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GameBoard extends JPanel {

  List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;
  private int numberOfPlayers = 4;// TODO->lang of playerList

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
      playerBoard.setBackground(Color.BLUE);
      playerBoardList.add(playerBoard);
      add(playerBoard, gameBoardConstrains);
    }
    GridBagConstraints playerBoardCons = new GridBagConstraints();
    playerBoardCons.insets = new Insets(10,10,10,10);
    setBackground(Color.GREEN);
    playerBoardCons.fill = GridBagConstraints.HORIZONTAL;
    playerBoardCons.gridx = 1;
    playerBoardCons.gridy = 1;
    playerBoardCons.gridwidth = 1;
    playerBoardCons.gridheight = 1;
    currentPlayerBoard = new PlayerBoard(300,600);
    add(currentPlayerBoard, playerBoardCons);

   /* GridBagConstraints wallCons = new GridBagConstraints();
    playerBoardCons.insets = new Insets(10,10,10,10);
    playerBoardCons.fill = GridBagConstraints.HORIZONTAL;
    playerBoardCons.gridx = 1;
    playerBoardCons.gridy = 0;
    playerBoardCons.gridwidth = 1;
    playerBoardCons.gridheight = 1;
    currentPlayerBoard = new PlayerBoard(300,600);
    add(currentPlayerBoard, playerBoardCons);*/

  }
}
