package azul.team12.view.board.playerBoard;

import azul.team12.controller.Controller;
import azul.team12.model.Offering;
import azul.team12.model.FactoryDisplay;
import jdk.internal.icu.text.UnicodeSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerBoard extends JPanel {
  Controller controller;
  BufferedImage image;
  private GridBagConstraints gbc;
  private JPanel patternLinesPanel, wallPanel;
  private JPanel playerStatus;
  private final String CARD;
  private final String nickname;
  private JLabel playerNameLabel,playerPointsLabel;
  public PlayerBoard(Controller controller, String nick) {
    this.controller = controller;
    nickname = nick;
    CARD = nick.toUpperCase();
    setLayout(new GridBagLayout());
    gbc = new GridBagConstraints();

    addPrimaryLayerPanels();
    addButtonsToPatternLinesPanel();
    addListenersToPatternLinesButtons();


    URL resource = getClass().getResource("/img/factory_display.png");
    try {
      image = ImageIO.read(resource);
      repaint();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  private void addButtonsToPatternLinesPanel() {
    for (int i = 1; i <= 5; i++) {
      for (int j = 1; j <= 5; j++) {
        patternLinesPanel.add(new JButton("pL " + String.valueOf(i) + " " + String.valueOf(j)));
        //TODO: disable unused Buttons, setOpaque().
      }
    }
  }
  private void addListenersToPatternLinesButtons() {
    for (Component component : patternLinesPanel.getComponents()) {
      JButton button = (JButton) component;
      button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String input = button.getName();
          String[] tokens = input.trim().split("\\s+");
          //TODO: calculate index of location on patternlines based on x y Cell coordinates in tokens and swap with 1
          //TODO: Change String FactoryDisplay to Type Offering
          controller.chooseTileFrom(controller.getNickOfActivePlayer(),1, new FactoryDisplay());
        }
      });
    }
  }
  private void addPrimaryLayerPanels() {
    patternLinesPanel = new JPanel();
    patternLinesPanel.setLayout(new GridBagLayout());
    patternLinesPanel.setBackground(Color.black);

    wallPanel = new JPanel();
    patternLinesPanel.setLayout(new GridBagLayout());
    wallPanel.setBackground(Color.white);

    Insets patternLinesWallInset = new Insets(10,10,10,5);

    addComponentWithGridBadConstraints(this,new JPanel(),null,0,0,2,1,GridBagConstraints.BOTH,1,0.1);
    addComponentWithGridBadConstraints(this,patternLinesPanel,patternLinesWallInset,0,1,1,1,GridBagConstraints.BOTH,0.5,0.9);
    addComponentWithGridBadConstraints(this,wallPanel,patternLinesWallInset,1,1,1,1,GridBagConstraints.BOTH,0.5,0.9);



  }


  private void createPlayerNameLabel() {
    playerNameLabel = new JLabel("Player Name: " + controller.getNickOfActivePlayer());
    playerStatus.add(playerNameLabel);
  }

  private void createPlayerPointsLabel() {
    playerPointsLabel = new JLabel("Points: " + controller.getPoints(nickname));
    playerStatus.add(playerPointsLabel);
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
  private void addComponentWithGridBadConstraints(JPanel panel, JComponent comp,Insets insets,
                                                  int x, int y, int width, int height,
                                                  int fill, double weightx, double weighty) {
    gbc.gridx = x;
    gbc.gridy = y;

    gbc.gridwidth = width;
    gbc.gridheight = height;
    gbc.fill = fill;
    gbc.weightx = weightx;
    gbc.weighty = weighty;

    if (insets != null) {
      gbc.insets = insets;
    }

    panel.add(comp, gbc);
  }

  public String getCARD() {
    return CARD;
  }

  /**
   * Used for painting background image later on.
   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, this);
  }


}
