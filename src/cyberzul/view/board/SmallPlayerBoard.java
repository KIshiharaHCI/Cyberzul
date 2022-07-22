package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A SmallPlayerBoard used for displaying opponent Players.
 */
public class SmallPlayerBoard extends PlayerBoard {
  @Serial
  private static final long serialVersionUID = 14L;
  private transient BufferedImage image;

  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param controller        the controller.
   * @param tileClickListener the tile click listener.
   * @param playerName        the name of the player of this small board.
   */
  public SmallPlayerBoard(
      Controller controller,
      TileClickListener tileClickListener,
      String playerName,
      Dimension panelDimension) {
    super(controller, tileClickListener, playerName, Tile.SMALL_TILE_SIZE, panelDimension);
    setOpaque(false);
    setSmallPlayerBoardSize(panelDimension);

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/smallhud.png");
      image = ImageIO.read(Objects.requireNonNull(imgUrl));
    } catch (IOException e) {
      e.printStackTrace();
    }
    JPanel wrapperForWrapper =
        new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
          }
        };
    wrapperForWrapper.setPreferredSize(new Dimension(300, 200));
    wrapperForWrapper.setOpaque(false);
    remove(playerBoardWrapper);
    wrapperForWrapper.add(playerBoardWrapper);
    add(wrapperForWrapper);
  }

  /**
   * Sets the properties of a PlayeBoard.
   *
   * @param panelDimension given Dimension of the whole left sidebar.
   */
  private void setSmallPlayerBoardSize(Dimension panelDimension) {
    panelDimension = new Dimension(280, 200);
    setMaximumSize(panelDimension);
    setMinimumSize(panelDimension);
  }
}
