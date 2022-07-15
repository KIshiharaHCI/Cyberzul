package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.model.TableCenter;
import azul.team12.view.listeners.TileClickListener;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The tiles on the center of the table ("Tischmitte") (Center Board class)
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "We are aware that data "
    + "encapsulation is violated here and that this is in principle bad. However, as here just "
    + "information of the view is possible to be changed from an external source and the "
    + "model is safe, we think it is ok to suppress this warning.")
public class TableCenterPanel extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private transient Controller controller;
  private static final int SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL = 13;
  private static final int SPACE_BETWEEN_TILES_IN_PXL = 44;
  private int SPACE_FROM_TOP_EDGE_IN_PXL = 13;
  private static final int plateId = 0;
  private int TABLE_CENTER_SIZE_WIDTH_IN_PXL = 1100;
  private int TABLE_CENTER_HEIGHT_IN_PXL = 260;
  private transient List<SourceTile> tileList;
  private JLabel tableCenterImageLabel;
  private transient TableCenter tableCenter;
  private Dimension panelDimension;

  /**
   * Constructor for Table Center Panel.
   * @param controller the Controller given to CyberzulView
   * @param tileClickListener listener class to be used when creating SourceTiles
   * @param panelDimension Dimensions of the parent Panel PlayerBoardAndTablePanel
   */
  public TableCenterPanel(Controller controller, TileClickListener tileClickListener, Dimension panelDimension) {
    this.controller = controller;
    this.tableCenter = (TableCenter) controller.getOfferings().get(0);
    this.tileList = new ArrayList<>();
    this.panelDimension = panelDimension;

    setTableCenterPanelSize();
    initialize(tileClickListener, tableCenter);

  }
  /**
   * Calculates the relative Size for "this" based on the Dimensions of PlayerBoardAndTableCenterPanel.
   */
  private void setTableCenterPanelSize() {
    panelDimension = new Dimension(
            (int) (panelDimension.getWidth() * 0.4),
            (int) (panelDimension.getHeight())
    );
    setMinimumSize(panelDimension);
    setMaximumSize(panelDimension);
  }

  public void initialize(TileClickListener tileClickListener,
      TableCenter tableCenter) {
    this.tableCenter = tableCenter;
    tableCenterImageLabel = new JLabel(
        getResizedImageIcon("img/table-center.png"));
    add(tableCenterImageLabel);
    tableCenterImageLabel.setBounds(0, 0, (int) panelDimension.getWidth(),
            (int) panelDimension.getHeight());
    List<ModelTile> modelTiles = tableCenter.getContent();
    for (int i = 0; i < modelTiles.size(); i++) {
      int col = i / 4 + 1;
      int row = i % 4 + 1;
      SourceTile tile = new SourceTile(col, row, modelTiles.get(i), i, plateId,Tile.NORMAL_TILE_SIZE, tileClickListener);
      //System.out.println("tile: " + tile);
      tileList.add(tile);
      tile.setBounds(SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL + ((row - 1) * SPACE_BETWEEN_TILES_IN_PXL),
              SPACE_FROM_TOP_EDGE_IN_PXL + SPACE_BETWEEN_TILES_IN_PXL * (col - 1), Tile.NORMAL_TILE_SIZE, Tile.NORMAL_TILE_SIZE);
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
    BufferedImage resizedimage = new BufferedImage(panelDimension.width,
        panelDimension.height,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedimage.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(icon1.getImage(), 0, 0, panelDimension.width, panelDimension.height,
        null);

    return new ImageIcon(resizedimage);
  }

  public JLabel getTableCenterImageLabel() {
    return tableCenterImageLabel;
  }
}
