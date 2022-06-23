package azul.team12.view.board;

import azul.team12.view.AzulView;
import azul.team12.view.board.playerBoard.Plate;
import azul.team12.view.board.playerBoard.Plates;
import azul.team12.view.board.playerBoard.PlayerBoard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameBoard extends JPanel {

  List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;

  List<Plate> plateList;
  private int numberOfPlayers = 4;// TODO->lang of playerList
  private int numberOfPlates;
  private int frameWidth = AzulView.WIDTH;
  private int frameHight = AzulView.HEIGHT;

  public GameBoard() {

    setPreferredSize(new Dimension(1200, 800));
    setLayout(new BorderLayout());
    // setBackground(new Color(110,90,120));
    createLeft();
    createCenter();
  }

  private void createCenter() {
    JPanel center = new JPanel();
    center.setLayout(new BorderLayout());
    numberOfPlates = numberOfPlayers * 2 + 1;
    plateList = new ArrayList<>();
    Plates plates = new Plates(numberOfPlates, this.plateList);
    center.add(plates, BorderLayout.CENTER);

    currentPlayerBoard = new PlayerBoard(300, 900);
    currentPlayerBoard.setBorder(new EmptyBorder(0, 80, 20, 80));

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
    add(othersPanel, BorderLayout.WEST);
  }

}
