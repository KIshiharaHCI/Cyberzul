package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * The PlayerBoard of the active player (at the lower center of the Gameboard).
 */
public class ActivePlayerBoard extends PlayerBoard {

  private static final long serialVersionUID = 15L;
  private final transient BufferedImage image;

  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param controller the game controller
   * @param tileClickListener listens to which tile is clicked
   * @param playerName the name of the player who's board it is
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

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/hud.png");
      image = ImageIO.read(Objects.requireNonNull(imgUrl));
      image.getScaledInstance(panelDimension.width, panelDimension.height, Image.SCALE_DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, null);
  }
}
