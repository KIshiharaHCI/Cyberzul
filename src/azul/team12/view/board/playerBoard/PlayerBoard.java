package azul.team12.view.board.playerBoard;

import azul.team12.controller.Controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PlayerBoard extends JPanel {
  Controller controller;
  BufferedImage image;
  private JPanel playerStatus;
  private JPanel playField;
  private JLabel playerNameLabel;
  private JLabel playerPointsLabel;
  private static final int initWidth = 400;
  private static final int initHeight = 400;
  private final String nickname;
  public PlayerBoard(Controller controller, String nick) {
    this.controller = controller;
    nickname = nick;

    URL resource = getClass().getResource("/img/factory_display.png");

    try {
      image = ImageIO.read(resource);
      repaint();

    } catch (IOException e) {
      e.printStackTrace();
    }

    createContentPane();
  }

  private void createContentPane() {
    playerStatus = new JPanel();
    playerStatus.setLayout(new BorderLayout());
    setMinimumSize(new Dimension(initWidth, initHeight));
    setPreferredSize(new Dimension(initWidth, initHeight));
    createPlayerNameLabel();
    createPlayerPointsLabel();
    add(playerStatus);

    createPlayField();

  }

  private void createPlayerNameLabel() {
    playerNameLabel = new JLabel("Player Name: " + controller.getNickOfActivePlayer());
    playerStatus.add(playerNameLabel);
  }
  private void createPlayerPointsLabel() {
    playerPointsLabel = new JLabel("Points: " + controller.getPoints(nickname));
    playerStatus.add(playerPointsLabel);
  }
  private void createPlayField() {
    playField = new JPanel(new GridLayout(5, 10));

    for (int i = 0; i < 50; ++i) {
      if ((5 <= i && i <= 10) || (9 <= i && i <= 20) || (13 <= i && i <= 30) || (17 <= i && i <= 50) ) {
        playField.add(new JButton(String.valueOf(i)));
      }
    }


  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, this);
  }

}
