package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.board.playerBoard.PlayerBoard;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.*;


public class GameBoard extends JPanel {
  Controller controller;
  private CardLayout layout;
  private GridBagConstraints gbc;

  //Largest primary layer Panels
  private JPanel topPanel,bottomPanel,chatPanel,centerPanel,sideBarPanel;
  //secondary Panels embedded in primary layer Panels
  private JPanel activePlayerBoard,factoryDisplayPanel,tableCenterPanel;
  private JPanel playerBoards;
  private JButton endTurnButton;
  public GameBoard(Controller controller) {
    this.controller = controller;
    setLayout(new GridBagLayout());
    setBackground(Color.magenta);

    initializeWidgets();
    createPlayerBoards();

    addPrimaryLayerPanels();
    addSecondaryLayerPanels();

  }
  private void addPrimaryLayerPanels() {
    sideBarPanel = new JPanel();
    sideBarPanel.setBackground(Color.blue.brighter().brighter());
    centerPanel = new JPanel();
    centerPanel.setLayout(new GridBagLayout());
    centerPanel.setBackground(Color.blue);
    chatPanel = new JPanel();
    chatPanel.setBackground(Color.red.darker());

    addComponentWithGridBadConstraints(this,topPanel,0,0,3,1,GridBagConstraints.BOTH,1,0.05);
    addComponentWithGridBadConstraints(this,bottomPanel,0,2,3,1,GridBagConstraints.BOTH,1,0.05);
    addComponentWithGridBadConstraints(this,sideBarPanel,0,1,1,1,GridBagConstraints.BOTH,0.15,0.85);
    addComponentWithGridBadConstraints(this,centerPanel,1,1,1,1,GridBagConstraints.BOTH,0.55,0.85);
    addComponentWithGridBadConstraints(this,chatPanel,2,1,1,1,GridBagConstraints.BOTH,0.2,0.85);

  }
  private void addSecondaryLayerPanels() {
    activePlayerBoard = new JPanel();
    activePlayerBoard.setBackground(Color.lightGray);

    factoryDisplayPanel = new JPanel();
    factoryDisplayPanel.setBackground(Color.green.darker());

    tableCenterPanel = new JPanel();
    tableCenterPanel.setBackground(Color.red.brighter().brighter());

    addComponentWithGridBadConstraints(centerPanel, activePlayerBoard,0,1,2,1,GridBagConstraints.BOTH,1.0,0.5);
    addComponentWithGridBadConstraints(centerPanel, factoryDisplayPanel,0,0,1,1,GridBagConstraints.BOTH,0.6,0.5);
    addComponentWithGridBadConstraints(centerPanel, tableCenterPanel,1,0,1,1,GridBagConstraints.BOTH,0.4,0.5);

    tableCenterPanel.add(playerBoards);
  }



  private void initializeWidgets() {
    layout = new CardLayout();
    gbc = new GridBagConstraints();

    //Panels
    topPanel = new JPanel();
    topPanel.setBackground(Color.yellow);
    bottomPanel = new JPanel();
    bottomPanel.setBackground(Color.green);
    playerBoards = new JPanel(layout);

    //Buttons
    endTurnButton = new JButton("End Turn");
    endTurnButton.setBackground(Color.red);
    endTurnButton.setContentAreaFilled(true);


  }
  private void createPlayerBoards() {
    for (int i = 0; i < controller.getPlayerNamesList().size(); i++) {
      PlayerBoard playerBoard = new PlayerBoard(controller, controller.getPlayerNamesList().get(i));
      playerBoards.add(playerBoard);
    }

  }

  /**
   * Used to
   *
   * @param panel JPanel (with GridBagLayout), on which components will be placed.
   * @param comp JComponent, which will be placed in the Panel.
   * @param x X-Coordinate Position on the Grid
   * @param y Y-Coordinate Position on the Grid
   * @param width Number of Cells on the Grid the Component will span in the X-Coordinate direction
   * @param height Number of Cells on the Grid the Component will span in the Y-Coordinate direction
   * @param fill Used when the component's display area is larger than the component's requested size
   *             to determine whether and how to resize the component
   * @param weightx This is the actual width the Component will occupy relative to all other components on the Panel
   * @param weighty This is the actual height the Component will occupy relative to all other components on the Panel
   */
  private void addComponentWithGridBadConstraints(JPanel panel, JComponent comp,
                                                  int x, int y, int width, int height,
                                                  int fill, double weightx, double weighty) {
    gbc.gridx = x;
    gbc.gridy = y;
    gbc.gridwidth = width;
    gbc.gridheight = height;
    gbc.fill = fill;
    gbc.weightx = weightx;
    gbc.weighty = weighty;

    panel.add(comp, gbc);
  }


}
