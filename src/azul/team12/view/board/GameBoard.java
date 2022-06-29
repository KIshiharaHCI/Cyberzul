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
  private final Controller controller;
  private CardLayout layout;
  private JPanel playerBoards;
  PlayerBoard currentPlayerBoard;
  private int panelDrawWidth;
  private int panelDrawHeight;
  FactoryDisplayBar displayBar;



  public GameBoard(Controller controller, int width, int height) {
    this.controller = controller;
    panelDrawWidth = width;
    panelDrawHeight = height;
    setPreferredSize(new Dimension(panelDrawWidth, panelDrawHeight));
    setLayout(new BorderLayout());

    initializeWidgets();
    createPlayerBoards();
    createEastPanel();
    createCenterPanel();
    setUpMouseClickListener();
  }
  private void initializeWidgets() {
    //TODO: Initialize End Turn Button
    playerBoards = new JPanel(layout);
  }
  private void createPlayerBoards() {
    for (int i = 0; i < controller.getPlayerNamesList().size(); i++) {
      PlayerBoard playerBoard = new PlayerBoard(controller, controller.getPlayerNamesList().get(i));
      playerBoards.add(playerBoard);
    }
  }



  /**
   * Creates the East Panel and populates it with the thumbnail view of Playerboards of the other players.
   *
   */
  private void createEastPanel() {
    JPanel thumbnails = new JPanel();
    thumbnails.setLayout(new BoxLayout(thumbnails, BoxLayout.Y_AXIS));
    thumbnails.setBackground(Color.magenta);

    for (int i = 0; i < controller.getPlayerNamesList().size() - 1; i++) {
      PlayerBoard playerBoard = new PlayerBoard(controller);
      thumbnails.add(playerBoard);
    }
    add(thumbnails);
  }

  /**
   * Creates the Panel which contains the Playerboard of the current player and the FactoryDisplay.
   */
  private void createCenterPanel() {
    JPanel mainPanel = new JPanel();


    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(Color.blue);
    displayBar = new FactoryDisplayBar();
    displayBar.setLayout(new GridLayout(3,3));

    mainPanel.add(displayBar, BorderLayout.NORTH);

    //TODO: change absolute pixel width height with relative sizing
    currentPlayerBoard = new PlayerBoard(controller,400, 300);
    currentPlayerBoard.setBorder(new EmptyBorder(0, 80, 20, 80));

    mainPanel.add(currentPlayerBoard, BorderLayout.SOUTH);
    add(mainPanel, BorderLayout.CENTER);
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
}
