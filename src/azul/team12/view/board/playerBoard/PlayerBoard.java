package azul.team12.view.board.playerBoard;

import azul.team12.controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerBoard extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private Controller controller;
  private int width;
  private int height;
  private String nickname;
  private JPanel playerBoard;
  private static final int NUMBER_PLAYER_BOARD_ROW = 5;
  private static final int NUMBER_PLAYER_BOARD_COL = 10;


  public PlayerBoard(Controller controller) {
    this.controller = controller;

  }


  public PlayerBoard(Controller controller, int width, int height) {
    this.controller = controller;
    this.width = width;
    this.height = height;

  }
  public PlayerBoard(Controller controller, String nickname) {
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    try {
      BufferedImage image = ImageIO.read(new File("img/playerboard.jpg"));
      g.drawImage(image,0,0,null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void createPlayerBoard() {

    playerBoard = new JPanel(new GridLayout(NUMBER_PLAYER_BOARD_ROW, NUMBER_PLAYER_BOARD_COL));

    for (int i = 1; i <= NUMBER_PLAYER_BOARD_ROW; i++) {
      for (int j = 1; j <= NUMBER_PLAYER_BOARD_COL; j++) {
        if (j >= NUMBER_PLAYER_BOARD_ROW - i) {
          if (j >= 5) {
            playerBoard.add(new JButton("pattern lines" + String.valueOf(i)));
          } else {
            playerBoard.add(new JButton("wall" + String.valueOf(i)));
          }
        } else {
          playerBoard.add(new JButton());
        }

      }
    }
  }

}
