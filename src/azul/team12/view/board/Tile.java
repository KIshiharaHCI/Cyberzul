package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The tiles on the GameBoard.
 */
public class Tile extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int tileId;
  static final int TILE_SIZE = 40;
  private final String BLACK_TILE_PATH = "img/black-tile.png";
  private final String BLUE_TILE_PATH = "img/blue-tile.png";
  private final String RED_TILE_PATH = "img/red-tile.png";
  private final String WHITE_TILE_PATH = "img/white-tile.png";
  //TODO: make yellow, and in model yellow too
  private final String ORANGE_TILE_PATH = "img/yellow-tile.png";
  private final String STARTING_PLAYER_MARKER_PATH = "img/start-player-button.png";


  private int plateId;
  private int cellSize;

  private ImageIcon icon;

  private JLabel label;

  /**
   * Creates a tile.
   *
   * TODO: ID is a index?
   * @param tileId
   * @param plateId
   * @param modelTile
   * @param tileClickListener
   */
  public Tile(int tileId, int plateId,
      ModelTile modelTile, TileClickListener tileClickListener) {
    setLayout(new GridLayout(1, 1));
    this.tileId = tileId;
    this.plateId = plateId;
    this.icon = setIcon(modelTile);
    setBorder(BorderFactory.createLineBorder(Color.black));
    setToolTipText(tileId + "");
    label = icon != null ? new JLabel(icon) : new JLabel("");
    add(label);

    this.addMouseListener(tileClickListener);
  }

  public int getTileId() {
    return tileId;
  }

  public ImageIcon getIcon() {
    return icon;
  }

  public ImageIcon setIcon(ModelTile modelTile) {
    switch (modelTile) {
      case BLACK_TILE -> {
        return getResizedImageIcon(BLACK_TILE_PATH);
      }
      case RED_TILE -> {
        return getResizedImageIcon(RED_TILE_PATH);
      }
      case BLUE_TILE -> {
        return getResizedImageIcon(BLUE_TILE_PATH);
      }
      case WHITE_TILE -> {
        return getResizedImageIcon(WHITE_TILE_PATH);
      }
      case ORANGE_TILE -> {
        return getResizedImageIcon(ORANGE_TILE_PATH);
      }
      case STARTING_PLAYER_MARKER -> {
        return getResizedImageIcon(STARTING_PLAYER_MARKER_PATH);
      }
      default ->
        throw new AssertionError("Unknown Tile!");

    }
  }

  public void setIcon(ImageIcon icon) {
    this.icon = icon;
  }

  public JLabel getLabel() {
    return label;
  }

  /**
   * Resizes according to the given TILE_SIZE and sets it according to the given path.
   * @param path path of the icon
   * @return ImageIcon with given TILE_SIZE and path
   */
  private ImageIcon getResizedImageIcon(String path) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    ImageIcon icon1 = new ImageIcon(imgURL1);
    BufferedImage resizedimage = new BufferedImage(TILE_SIZE, TILE_SIZE,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedimage.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(icon1.getImage(), 0, 0, TILE_SIZE, TILE_SIZE, null);

    return new ImageIcon(resizedimage);
  }

  public int getPlateId() {
    return plateId;
  }
}
