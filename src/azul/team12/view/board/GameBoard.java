package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.board.playerBoard.PlayerBoard;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;


public class GameBoard extends JPanel {
  Controller controller;
  //activePlayerBoard JPanel (with CardLayout) makes it possible to swap between the different PlayerBoards
  private CardLayout layout;
  private GridBagConstraints gbc;

  //Largest primary layer Panels
  private JPanel topPanel,bottomPanel,chatPanel,centerPanel,sideBarPanel;
  //secondary Panels embedded in primary layer Panels
  //TODO: migrate secondary Panels into suitable Classes
  private JPanel activePlayerBoard,factoryDisplayPanel,tableCenterPanel;
  //tertiary Panels
  //TODO: Add these to PlayerBoard
  private JPanel patternLinesPanel, wallPanel;
  private JPanel playerBoards;
  private JButton endTurnButton;

  public GameBoard(Controller controller) {
    this.controller = controller;
    setLayout(new GridBagLayout());

    //TODO: remove setBackground(),add setOpaque() and paint the image assets
    // (temporary fix to see shape and size of each Panel when debugging)
    setBackground(Color.magenta);

    initializeWidgets();
    createPlayerBoards();

    addPrimaryLayerPanels();
    addSecondaryLayerPanels();

  }

  /**
   * Uses GridBagConstraints and adds the Primary Panels to GameBoard JPanel.
   */
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

  /**
   * Uses GridBagConstraints to add the Panels which are contained within centerPanel.
   * Secondary Panels should be refactored into their own classes in the future.
   */
  private void addSecondaryLayerPanels() {
    activePlayerBoard = new JPanel();
    activePlayerBoard.setLayout(new GridBagLayout());
    activePlayerBoard.setBackground(Color.lightGray);

    factoryDisplayPanel = new JPanel();
    factoryDisplayPanel.setBackground(Color.green.darker());

    tableCenterPanel = new JPanel();
    tableCenterPanel.setBackground(Color.red.brighter().brighter());

    addComponentWithGridBadConstraints(centerPanel, activePlayerBoard,0,1,2,1,GridBagConstraints.BOTH,1.0,0.5);
    addComponentWithGridBadConstraints(centerPanel, factoryDisplayPanel,0,0,1,1,GridBagConstraints.BOTH,0.6,0.5);
    addComponentWithGridBadConstraints(centerPanel, tableCenterPanel,1,0,1,1,GridBagConstraints.BOTH,0.4,0.5);

    addComponentWithGridBadConstraints(activePlayerBoard, playerBoards,1,1,1,1,GridBagConstraints.BOTH,1,1);
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


  }
  private void createPlayerBoards() {
    for (int i = 0; i < controller.getPlayerNamesList().size(); i++) {
      PlayerBoard playerBoard = new PlayerBoard(controller, controller.getPlayerNamesList().get(i));
      playerBoards.add(playerBoard);
    }

  }

  /**
   * Used to add the constraints for the JComponent such as JButtons,JPanels,JLabels before adding it into the Panel.
   *        (it is possible to reuse the same GridBagConstraints instance for multiple components,
   *        even if the components have different constraints.)
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
