package azul.team12.view.board;

import azul.team12.view.board.playerBoard.Plates;
import azul.team12.view.board.playerBoard.PlayerBoard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  //List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;
  //  List<Plate> plateList;
  private int numberOfPlayers = 4;// TODO->lang of playerList
  private int numberOfPlates;

  private int panelDrawWidth;
  private int panelDrawHeight;

  public GameBoard(int width, int height) {

    panelDrawWidth = width;
    panelDrawHeight = height;
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
//    plateList = new ArrayList<>();
//    Plates plates = new Plates(numberOfPlates, this.plateList);
    Plates plates = new Plates(numberOfPlates);
    center.add(plates, BorderLayout.CENTER);

    currentPlayerBoard = new PlayerBoard(400, 300);
    currentPlayerBoard.setBorder(new EmptyBorder(0, 80, 20, 80));

    center.add(currentPlayerBoard, BorderLayout.SOUTH);
    add(center, BorderLayout.CENTER);
  }

  private void createLeft() {
    JPanel othersPanel = new JPanel();
    othersPanel.setLayout(new BoxLayout(othersPanel, BoxLayout.Y_AXIS));

    for (int i = 0; i < numberOfPlayers - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard();

      //playerBoardList.add(playerBoard);
      othersPanel.add(playerBoard);
    }
    add(othersPanel, BorderLayout.WEST);
  }

}
