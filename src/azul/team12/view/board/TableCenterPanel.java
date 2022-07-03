package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.model.TableCenter;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The tiles on the center of the table ("Tischmitte") (Center Board class)
 */
public class TableCenterPanel extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private int TABLE_CENTER_SIZE_WIDTH_IN_PXL = 1100;
  private int TABLE_CENTER_HEIGHT_IN_PXL = 260;

  private final int SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL = 13;
  private final int SPACE_BETWEEN_TILES_IN_PXL = 44;

  private List<Tile> tileList;
  private final int plateId = 0;

  public TableCenterPanel(TableCenter tableCenter, TileClickListener tileClickListener) {
    JLabel tableCenterImageLabel = new JLabel(
        getResizedImageIcon("img/table-center.png"));
    add(tableCenterImageLabel);
    tableCenterImageLabel.setBounds(0, 0, TABLE_CENTER_SIZE_WIDTH_IN_PXL,
        TABLE_CENTER_HEIGHT_IN_PXL);

    this.tileList = new ArrayList<>();
    List<ModelTile> modelTiles = tableCenter.getContent();

    for (int i = 0; i < modelTiles.size(); i++) {
      Tile tile = new Tile(i, this.plateId, modelTiles.get(i), tileClickListener);
      System.out.println("tile: " + tile);
      tileList.add(tile);
      if (i < 18) {
        tile.setBounds(SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL + (i * SPACE_BETWEEN_TILES_IN_PXL),
            SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL, Tile.TILE_SIZE, Tile.TILE_SIZE);
      } else {
        tile.setBounds(
            SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL + ((i - 18) * SPACE_BETWEEN_TILES_IN_PXL),
            SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL, Tile.TILE_SIZE, Tile.TILE_SIZE);

      }
      tableCenterImageLabel.add(tile);
    }

  }

  /**
   * Resizes according to the given height and width of the table center.
   *
   * @param path path of the icon.
   * @return ImageIcon with given width and height.
   */
  private ImageIcon getResizedImageIcon(String path) {
    URL imgURL = getClass().getClassLoader().getResource(path);
    ImageIcon icon1 = new ImageIcon(imgURL);
    BufferedImage resizedimage = new BufferedImage(TABLE_CENTER_SIZE_WIDTH_IN_PXL,
        TABLE_CENTER_HEIGHT_IN_PXL,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedimage.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(icon1.getImage(), 0, 0, TABLE_CENTER_SIZE_WIDTH_IN_PXL, TABLE_CENTER_HEIGHT_IN_PXL,
        null);

    return new ImageIcon(resizedimage);
  }


}
