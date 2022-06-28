package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.board.playerBoard.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GameBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  //List<PlayerBoard> playerBoardList = new ArrayList<>();
  PlayerBoard currentPlayerBoard;
  String currentPlayer;
  List<PlayerBoard> otherPlayersBoard;
  //  List<Plate> plateList;
  private int numberOfPlayers = 4;// TODO->lang of playerList
  private int numberOfPlates;

  private int panelDrawWidth;
  private int panelDrawHeight;
  FactoryDisplayBar displayBar;

  private final Controller controller;

  public GameBoard(Controller controller, int width, int height) {
    this.controller = controller;
    panelDrawWidth = width;
    panelDrawHeight = height;
    setPreferredSize(new Dimension(panelDrawWidth, panelDrawHeight));
    setLayout(new BorderLayout());

    createSideBar();
    createMainPanel();
    setUpMouseClickListener();
    setUpMouseMotionListener();
  }

  public void setUpMouseClickListener() {
    addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                //TODO: TEST TO PRINT OUT A POINT
                System.out.println("(" + e.getPoint().x + "," + e.getPoint().y + ")");
              }
            }
    );
  }

  /**
   * Add the mouse motion listener for player.
   * The Player can drag and drop the tiles with the same colour from the factory display or the middle of the table.
   */

  private void setUpMouseMotionListener() {
    addMouseListener(
            new MouseAdapter() {
              @Override
              public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (checkOfferingUnderMouse(e.getPoint())) {
                  //TODO: controller.chooseTileFrom();
                }
              }
              @Override
              public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

              }
              @Override
              public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

              }
            }

    );
  }

  /**
   * Check whether a MouseClick corresponds to a coordinate on an Offering.
   * @param point The point of the registered mouse click event.
   * @return
   */
  private boolean checkOfferingUnderMouse(Point point) {
    //if (displayBar.getParent().getLocation())


    return false;
  }


  /**
   * Creates the sidebar and populates it with the thumbnail view of Playerboards of the other players.
   *
   */
  private void createSideBar() {
    JPanel sidebar = new JPanel();
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

    for (int i = 0; i < numberOfPlayers - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard(controller);
      sidebar.add(playerBoard);
    }
    add(sidebar, BorderLayout.WEST);
  }

  /**
   * Creates the Panel which contains the Playerboard of the current player and the FactoryDisplayBar.
   */
  private void createMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    numberOfPlates = controller.getFactoryDisplays().size();

    displayBar = new FactoryDisplayBar(numberOfPlates);
    mainPanel.add(displayBar, BorderLayout.CENTER);

    //TODO: change absolute pixel width height with relative sizing
    currentPlayerBoard = new PlayerBoard(controller,400, 300);
    currentPlayerBoard.setBorder(new EmptyBorder(0, 80, 20, 80));

    mainPanel.add(currentPlayerBoard, BorderLayout.SOUTH);
    add(mainPanel, BorderLayout.CENTER);
  }



}
