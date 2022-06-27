package azul.team12.view.board;

import azul.team12.view.board.playerBoard.FactoryDisplayBar;
import azul.team12.view.board.playerBoard.PlayerBoard;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  //List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;
  List<PlayerBoard> otherPlayersBoard;
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
    createSideBar();
    createMainPanel();
  }

  /**
   * Creates the sidebar and populates it with the thumbnail view of Playerboards of the other players.
   *
   */
  private void createSideBar() {
    JPanel sidebar = new JPanel();
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

    for (int i = 0; i < numberOfPlayers - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard();
      sidebar.add(playerBoard);
    }
    add(sidebar, BorderLayout.WEST);
  }

  /**
   * Creates the Panel which contains the Playerboard of the current player and the manufacturing plates bar.
   */
  private void createMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    //TODO: get number of Factory displays in model and add them to the FactoryDisplayBar
    numberOfPlates = numberOfPlayers * 2 + 1;

    FactoryDisplayBar displayBar = new FactoryDisplayBar(numberOfPlates);
    mainPanel.add(displayBar, BorderLayout.CENTER);

    currentPlayerBoard = new PlayerBoard(400, 300);
    currentPlayerBoard.setBorder(new EmptyBorder(0, 80, 20, 80));

    mainPanel.add(currentPlayerBoard, BorderLayout.SOUTH);
    add(mainPanel, BorderLayout.CENTER);
  }



}
