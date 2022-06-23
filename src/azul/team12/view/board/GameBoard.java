package azul.team12.view.board;

import azul.team12.view.AzulView;
import azul.team12.view.board.playerBoard.Plates;
import azul.team12.view.board.playerBoard.PlayerBoard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GameBoard extends JPanel {

  List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;
  private int numberOfPlayers = 4;// TODO->lang of playerList
  private int numberOfPlates;
  private int frameWidth = AzulView.WIDTH;
  private int frameHight = AzulView.HEIGHT;

  public GameBoard() {

    setPreferredSize(new Dimension(1000, 800));
    setLayout(new BorderLayout());
   // setBackground(new Color(110,90,120));
    createLeft();
    createCenter();
  }

  private void createCenter() {
    JPanel center = new JPanel();
    center.setLayout(new BorderLayout());
    currentPlayerBoard = new PlayerBoard(300,900);
    numberOfPlates = numberOfPlayers * 2 + 1;
    Plates plates = new Plates(numberOfPlates);
    center.add(plates, BorderLayout.CENTER);
    center.add(currentPlayerBoard, BorderLayout.SOUTH);
    add(center, BorderLayout.CENTER);
  }

  private void createLeft() {
    JPanel othersPanel = new JPanel();
    othersPanel.setLayout(new BoxLayout(othersPanel, BoxLayout.Y_AXIS));

    for (int i = 0; i < numberOfPlayers - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard();

      playerBoardList.add(playerBoard);
      othersPanel.add(playerBoard);
    }
    add(othersPanel,BorderLayout.WEST);
  }

  //  public GameBoard(int numberOfPlayers) {
//    this.numberOfPlayers = numberOfPlayers;
//  }

  //  public GameBoard(GridBagLayout gridBagLayout) {
//    super(gridBagLayout);
//    setSize(new Dimension(1200, 860));
//    setBackground(Color.GREEN);
//
//    for (int i = 0; i < numberOfPlayers - 1; i++) {
//      GridBagConstraints gameBoardConstrains = new GridBagConstraints();
//      gameBoardConstrains.insets = new Insets(15,30,15,30);
//      gameBoardConstrains.fill = GridBagConstraints.HORIZONTAL;
//      gameBoardConstrains.gridx = 0;
//      gameBoardConstrains.gridy = i;
//      gameBoardConstrains.gridwidth = 1;
//      gameBoardConstrains.gridheight = 1;
//      PlayerBoard playerBoard = new PlayerBoard();
//      playerBoard.setBackground(Color.BLUE);
//      playerBoardList.add(playerBoard);
//      add(playerBoard, gameBoardConstrains);
//    }
//    GridBagConstraints playerBoardCons = new GridBagConstraints();
//    playerBoardCons.insets = new Insets(30,30,30,30);
//    playerBoardCons.fill = GridBagConstraints.HORIZONTAL;
//    playerBoardCons.gridx = 1;
//    playerBoardCons.gridy = 2;
//    playerBoardCons.gridwidth = 1;
//    playerBoardCons.gridheight = 1;
//    currentPlayerBoard = new PlayerBoard(300,600);
//    add(currentPlayerBoard, playerBoardCons);
//
//   /* GridBagConstraints wallCons = new GridBagConstraints();
//    playerBoardCons.insets = new Insets(10,10,10,10);
//    playerBoardCons.fill = GridBagConstraints.HORIZONTAL;
//    playerBoardCons.gridx = 1;
//    playerBoardCons.gridy = 0;
//    playerBoardCons.gridwidth = 1;
//    playerBoardCons.gridheight = 1;
//    currentPlayerBoard = new PlayerBoard(300,600);
//    add(currentPlayerBoard, playerBoardCons);*/
//
//  }
}
