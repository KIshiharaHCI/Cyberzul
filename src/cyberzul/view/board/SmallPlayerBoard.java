package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.view.listeners.TileClickListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.Objects;

/**
 * A SmallPlayerBoard used for displaying opponent Players.
 */
public class SmallPlayerBoard extends PlayerBoard {
  @Serial private static final long serialVersionUID = 14L;
  private final transient BufferedImage image;

  /**
   * The constructor to create a playerboard for a given player.
   *
   * @param controller
   * @param tileClickListener
   * @param playerName
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
      throw new RuntimeException(e);
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
   * @param panelDimension given Dimension of the whole left sidebar.
   */
  private void setSmallPlayerBoardSize(Dimension panelDimension) {
    panelDimension = new Dimension(280, 200);
    setMaximumSize(panelDimension);
    setMinimumSize(panelDimension);
  }
}
