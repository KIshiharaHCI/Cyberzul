package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.listeners.TileClickListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * The PlayerBoard of the active player (at the lower center of the Gameboard).
 */
public class ActivePlayerBoard extends PlayerBoard {

  private static final long serialVersionUID = 15L;
  private final transient BufferedImage image;

  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param controller        the game controller
   * @param tileClickListener listens to which tile is clicked
   * @param playerName        the name of the player who's board it is
   */
  public ActivePlayerBoard(
      Controller controller,
      TileClickListener tileClickListener,
      String playerName,
      Dimension panelDimension) {
    super(controller, tileClickListener, playerName, Tile.NORMAL_TILE_SIZE, panelDimension);
    setOpaque(false);

    setMaximumSize(panelDimension);
    setMinimumSize(panelDimension);
    add(Box.createVerticalStrut(500));

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/hud.png");
      image = ImageIO.read(Objects.requireNonNull(imgUrl));
      image.getScaledInstance(panelDimension.width + 200, panelDimension.height + 200, Image.SCALE_DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 60, 80, null);
  }
}
