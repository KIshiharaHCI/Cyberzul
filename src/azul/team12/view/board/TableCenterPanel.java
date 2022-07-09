package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.model.TableCenter;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The tiles on the center of the table ("Tischmitte") (Center Board class)
 */
public class TableCenterPanel extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final int SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL = 13;
  private final int SPACE_BETWEEN_TILES_IN_PXL = 44;
  private final int plateId = 0;
  private int TABLE_CENTER_SIZE_WIDTH_IN_PXL = 1100;
  private int TABLE_CENTER_HEIGHT_IN_PXL = 260;
  private List<SourceTile> tileList;
  private JLabel tableCenterImageLabel;
  private TableCenter tableCenter;

  public TableCenterPanel(TableCenter tableCenter, TileClickListener tileClickListener) {
    this.tableCenter = tableCenter;
    this.tileList = new ArrayList<>();

    initialize(tileClickListener, tableCenter);

  }

  public void initialize(TileClickListener tileClickListener,
      TableCenter tableCenter) {
    this.tableCenter = tableCenter;
    tableCenterImageLabel = new JLabel(
        getResizedImageIcon("img/table-center.png"));
    add(tableCenterImageLabel);
    tableCenterImageLabel.setBounds(0, 0, TABLE_CENTER_SIZE_WIDTH_IN_PXL,
        TABLE_CENTER_HEIGHT_IN_PXL);
    List<ModelTile> modelTiles = tableCenter.getContent();
    for (int i = 0; i < modelTiles.size(); i++) {
      int col = i / 18 + 1;
      int row = i % 18 + 1;
      SourceTile tile = new SourceTile(col, row, modelTiles.get(i), i, plateId,Tile.NORMAL_TILE_SIZE, tileClickListener);
      //System.out.println("tile: " + tile);
      tileList.add(tile);
      if (i < 18) {
        tile.setBounds(SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL + (i * SPACE_BETWEEN_TILES_IN_PXL),
            SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL, Tile.NORMAL_TILE_SIZE, Tile.NORMAL_TILE_SIZE);
      } else {
        tile.setBounds(
            SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL + ((i - 18) * SPACE_BETWEEN_TILES_IN_PXL),
            SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL, Tile.NORMAL_TILE_SIZE, Tile.NORMAL_TILE_SIZE);
      }
      tableCenterImageLabel.add(tile);
    }
  }

  public void remove() {
    for (SourceTile tile : this.tileList) {
      this.tableCenterImageLabel.remove(tile);
      this.remove(tableCenterImageLabel);
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

  public JLabel getTableCenterImageLabel() {
    return tableCenterImageLabel;
  }
}
